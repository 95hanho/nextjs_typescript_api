package me._hanho.nextjs_shop.product;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import me._hanho.nextjs_shop.common.util.MaskingUtil;
import me._hanho.nextjs_shop.model.ProductQnaType;
import me._hanho.nextjs_shop.model.UserCoupon;
import me._hanho.nextjs_shop.mypage.dto.ReviewImageResponse;
import me._hanho.nextjs_shop.product.dto.AddCartItem;
import me._hanho.nextjs_shop.product.dto.AddCartRequest;
import me._hanho.nextjs_shop.product.dto.AvailableProductCouponResponse;
import me._hanho.nextjs_shop.product.dto.CartAddResult;
import me._hanho.nextjs_shop.product.dto.CartAppliedRow;
import me._hanho.nextjs_shop.product.dto.CartQtyRow;
import me._hanho.nextjs_shop.product.dto.GetProductListRequest;
import me._hanho.nextjs_shop.product.dto.GetProductListResponse;
import me._hanho.nextjs_shop.product.dto.OtherProduct;
import me._hanho.nextjs_shop.product.dto.ProductDetailResponse;
import me._hanho.nextjs_shop.product.dto.ProductImageFile;
import me._hanho.nextjs_shop.product.dto.ProductListCursorResponse;
import me._hanho.nextjs_shop.product.dto.ProductListResponse;
import me._hanho.nextjs_shop.product.dto.ProductOptionResponse;
import me._hanho.nextjs_shop.product.dto.ProductQnaRequest;
import me._hanho.nextjs_shop.product.dto.ProductQnaResponse;
import me._hanho.nextjs_shop.product.dto.ProductReviewResponse;
import me._hanho.nextjs_shop.product.dto.ProductReviewSummary;
import me._hanho.nextjs_shop.product.dto.UpdateProductQnaRequest;

@Service
@RequiredArgsConstructor
public class ProductService {
	
//	private static final Logger logger = LoggerFactory.getLogger(ProductService.class);
	private static final int PRODUCT_LIST_PAGE_SIZE = 12;

	private final ProductMapper productMapper;
	
	public GetProductListResponse getProductList(GetProductListRequest request) {
		 Timestamp lastCreatedAt = null;
		if (request.getLastCreatedAt() != null && !request.getLastCreatedAt().isBlank()) {
			lastCreatedAt = Timestamp.from(OffsetDateTime.parse(request.getLastCreatedAt()).toInstant());
		}

		List<ProductListResponse> productList = productMapper.getProductList(request, lastCreatedAt, PRODUCT_LIST_PAGE_SIZE + 1);

		if (productList.isEmpty()) {
			return new GetProductListResponse(
					java.util.Collections.emptyList(),
					false,
					null
			);
		}

		// ✅ 1) 다음 페이지 존재 여부 확인용으로 size+1 조회했다는 가정
		boolean hasNext = productList.size() > PRODUCT_LIST_PAGE_SIZE;

		// ✅ 2) 응답용 리스트는 실제 노출 개수만 자르기
		List<ProductListResponse> visibleList = hasNext
				? productList.subList(0, PRODUCT_LIST_PAGE_SIZE)
				: productList;

		// ✅ 3) productId 목록 추출
		List<Integer> productIds = visibleList.stream()
				.map(ProductListResponse::getProductId)
				.toList();

		// ✅ 4) 이미지들을 "한 방"에 가져오기 (쿼리 1번)
		List<ProductImageFile> allImages = productMapper.getProductImageListByProductIds(productIds);

		// ✅ 5) productId로 그룹핑
		Map<Integer, List<ProductImageFile>> imageMap = allImages.stream()
				.collect(java.util.stream.Collectors.groupingBy(ProductImageFile::getProductId));

		// ✅ 6) DTO에 주입 (없으면 빈 리스트)
		for (ProductListResponse p : visibleList) {
			p.setProductImageList(imageMap.getOrDefault(p.getProductId(), java.util.Collections.emptyList()));
		}

		// ✅ 7) nextCursor 생성
		ProductListCursorResponse nextCursor = null;
		if (hasNext && !visibleList.isEmpty()) {
			ProductListResponse lastItem = visibleList.get(visibleList.size() - 1);

			nextCursor = new ProductListCursorResponse();
			nextCursor.setLastProductId(lastItem.getProductId());

			switch (request.getSort()) {
				case "POPULAR":
					nextCursor.setLastPopularity(lastItem.getViewCount() + lastItem.getWishCount());
					break;
				case "LATEST":
					nextCursor.setLastCreatedAt(lastItem.getCreatedAt());
					break;
				case "PRICE_LOW":
				case "PRICE_HIGH":
					nextCursor.setLastPrice(lastItem.getFinalPrice());
					break;
			}
		}

		return new GetProductListResponse(visibleList, hasNext, nextCursor);
	}
	@Transactional
	public void setLike(Integer productId, Integer userNo) {
		boolean hasLike = productMapper.isLikeExist(productId, userNo);
		if(!hasLike) {
			productMapper.upProductLike(productId);
			productMapper.insertLike(productId, userNo);
		} else {
			productMapper.downProductLike(productId);
			productMapper.deleteLike(productId, userNo);
		}
	}

	public List<Integer> getProductWishList(Integer userNo) {
		return productMapper.getProductWishList(userNo);
	}
	
	@Transactional
	public void setWish(Integer productId, Integer userNo) {
		boolean hasWish = productMapper.isWishExist(productId, userNo);
		if(!hasWish) {
			productMapper.upProductWish(productId);
			productMapper.insertWish(productId, userNo);
		} else {
			productMapper.downProductWish(productId);
			productMapper.deleteWish(productId, userNo);
		}
	}

	public boolean getProductHasCart (Integer productId, Integer userNo) {
		List<Integer> addCartList = productMapper.getProductCart(productId, userNo);
		return addCartList != null && !addCartList.isEmpty();
	}

	@Transactional
	public CartAddResult addCart(AddCartRequest addCartRequest, Integer userNo) {
		List<Integer> existingOptionIds = productMapper.getProductCart(addCartRequest.getProductId(), userNo);

		// 이미 장바구니에 있으면 업데이트, 없으면 추가
		List<AddCartItem> updateList = new ArrayList<>();
		List<AddCartItem> insertList = new ArrayList<>();
		for (AddCartItem cart : addCartRequest.getAddCartList()) {
			if (existingOptionIds.contains(cart.getProductOptionId())) {
				updateList.add(cart);
			} else {
				insertList.add(cart);
			}
		}

		// ✅ update 전 수량 미리 확보 (update 판단용)
		Map<Integer, Integer> beforeQtyMap = new HashMap<>();
		if (!updateList.isEmpty()) {
			List<CartQtyRow> beforeRows = productMapper.getCartQtyMap(userNo, updateList);
			for (CartQtyRow r : beforeRows) {
				beforeQtyMap.put(r.getProductOptionId(), r.getQuantity());
			}
		}

		int result = 0;

		if (!updateList.isEmpty()) {
			result = result + productMapper.upQuantityCart(updateList, userNo);
		}
		if (!insertList.isEmpty()) {
			result = result + productMapper.addCart(insertList, userNo);
		}

		// after + stock + requested 조회
		List<CartAppliedRow> applied = productMapper.getCartAppliedResult(userNo, addCartRequest.getAddCartList());

		int successCount = 0;
		List<Integer> limitedItems = new ArrayList<>();

		Map<Integer, CartAppliedRow> appliedMap = new HashMap<>();
		for (CartAppliedRow row : applied) {
			appliedMap.put(row.getProductOptionId(), row);
		}

		for (AddCartItem reqItem : addCartRequest.getAddCartList()) {
			CartAppliedRow row = appliedMap.get(reqItem.getProductOptionId());

			// cart에 없으면 (재고 0 등) 성공 카운트 안 함
			if (row == null || row.getCartQuantity() == null) {
				continue;
			}

			Integer beforeQty = beforeQtyMap.get(reqItem.getProductOptionId());
			int before = (beforeQty == null ? 0 : beforeQty);
			int after = row.getCartQuantity();
			int appliedDelta = after - before; // ✅ 이번 요청으로 실제 증가분

			if (appliedDelta > 0) successCount++;
			if (appliedDelta < reqItem.getQuantity()) {
				limitedItems.add(reqItem.getProductOptionId());
			}
		}

		CartAddResult res = new CartAddResult();
		res.setSuccessCount(successCount);
		res.setPartial(!limitedItems.isEmpty());
		res.setLimitedItems(limitedItems);
		return res;
	}
	
	public ProductDetailResponse getProductDetail(int productId, Integer userNo) {
		ProductDetailResponse productDetail = productMapper.getProductDetail(productId);
		
		productDetail.setProductImageList(
	        productMapper.getProductImageList(productId)
	    );

		// 조회수 업
		productMapper.upProductHit(productId);
		// nextjs_shop_product_view insert
		if (userNo != null) {
			productMapper.insertProductView(productId, userNo);
		}
		
		return productDetail;
	}

	public List<ProductOptionResponse> getProductOptionList(int productId) {
		return productMapper.getProductOptionList(productId);
	}

	public List<AvailableProductCouponResponse> getAvailableProductCoupon(int productId, Integer userNo) {
		return productMapper.getAvailableProductCoupon(productId, userNo);
	}

	public List<ProductImageFile> getProductDetailImage(int productId) {
		return productMapper.getProductDetailImage(productId);
	}

	public boolean isSellerLiked(int productId, Integer userNo) {
		return productMapper.isSellerLikeExist(productId, userNo) > 0;
	}

	public List<OtherProduct> getSellerOtherProducts(int productId, Integer userNo) {
		return productMapper.getSellerOtherProducts(productId, userNo);
	}

	@Transactional
	public void setSellerLike(Integer productId, Integer userNo, Boolean like) {
		boolean hasLike = productMapper.isSellerLikeExist(productId, userNo) > 0;
		if (like && !hasLike) {
			productMapper.upSellerLike(productId);
			productMapper.insertSellerLike(productId, userNo);
		} else if (!like && hasLike) {
			productMapper.downSellerLike(productId);
			productMapper.deleteSellerLike(productId, userNo);
		}
	}

	public int getProductReviewCount(Integer productId) {
		return productMapper.getProductReviewCount(productId);
	}
	
	public List<ProductReviewResponse> getProductReviewList(Integer productId, int offset, int size, Integer userNo) {
		
		List<ProductReviewResponse> list = productMapper.getProductReviewList(productId, offset, size, userNo);
		
		// 제품 리뷰 이미지 조회
		List<ReviewImageResponse> allReviewImages = productMapper.getProductReviewImageListByProductId(productId);

		if (list == null || list.isEmpty()) return list;
		
		for (ProductReviewResponse review : list) {
	        Integer writerNo = review.getUserNo();
	        review.setUserNo(null);
	        boolean isOwner = userNo != null && writerNo != null && writerNo.equals(userNo);

	        // 1) 작성자가 아니면 userId 마스킹(앞 2글자 + *****)
	        if (!isOwner) {
	        	review.setUserName(MaskingUtil.maskUserIdName(review.getUserName(), 5));
	        }
			// 2) 리뷰 이미지 주입
	        List<ReviewImageResponse> reviewImages = allReviewImages.stream()
	                .filter(image -> image.getReviewId() == review.getReviewId())
	                .toList();
	        review.setReviewImages(reviewImages);
	    }

	    return list;
	}

	public List<ReviewImageResponse> getProductInitReviewImageListByProductId(Integer productId) {
		return productMapper.getProductInitReviewImageListByProductId(productId);
	}

	public void deleteProductReview(int reviewId, Integer userNo) {
		productMapper.deleteProductReview(reviewId, userNo);
	}
	
	public ProductReviewSummary getProductReviewSummary(Integer productId) {
		return productMapper.getProductReviewSummary(productId);
	}

	public List<ProductQnaResponse> getProductQnaList(int productId, Integer userNo) {
	    List<ProductQnaResponse> list = productMapper.getProductQnaList(productId, userNo);

	    if (list == null || list.isEmpty()) return list;

	    for (ProductQnaResponse qna : list) {
	        Integer writerNo = qna.getUserNo();
	        qna.setUserNo(null);
	        boolean isOwner = userNo != null && writerNo != null && writerNo.equals(userNo);

	        // 1) 작성자가 아니면 userId 마스킹(앞 2글자 + *****)
	        if (!isOwner) {
	        	qna.setUserName(MaskingUtil.maskUserIdName(qna.getUserName(), 5));
	        }

	        // 2) 비밀글(secret=1)인데 작성자가 아니면 질문/답변 null 처리
	        //    (판매자/관리자도 원문 열람 가능하게 하려면 여기에서 권한 조건 추가하면 됨)
	        if (!isOwner && Boolean.TRUE.equals(qna.isSecret())) {
	            qna.setQuestion(null);
	            qna.setAnswer(null);
	            // 필요하면 응답시간도 숨김
	            // qna.setResCreatedAt(null);
	        }
	    }

	    return list;
	}
	
	public List<ProductQnaType> getProductQnaTypeList() {
		return productMapper.getProductQnaTypeList();
	}

	public void createProductQna(ProductQnaRequest productQnaRequest, Integer productId, Integer userNo) {
		productMapper.insertProductQna(productQnaRequest, productId, userNo);
	}

	public void updateProductQna(UpdateProductQnaRequest productQnaRequest, Integer userNo) {
		productMapper.updateProductQna(productQnaRequest, userNo);
	}

	public void deleteProductQna(int productQnaId, Integer userNo) {
		productMapper.updateProductQnaDelete(productQnaId, userNo);
	}

	public void updateProductQnaRead(int productQnaId, Integer userNo) {
		productMapper.updateProductQnaRead(productQnaId, userNo);
	}

	public List<OtherProduct> getCategoryBestProductList(int productId, Integer userNo) {
		return productMapper.getCategoryBestProductList(productId, userNo);
	}

	public int couponDownload(UserCoupon userCoupon) {
		int result = productMapper.couponDownload(userCoupon);
		if(result == 0) return 0;

		return userCoupon.getUserCouponId();
	}

}
