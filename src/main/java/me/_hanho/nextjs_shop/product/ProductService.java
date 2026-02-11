package me._hanho.nextjs_shop.product;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import me._hanho.nextjs_shop.common.util.MaskingUtil;

@Service
@RequiredArgsConstructor
public class ProductService {
	
//	private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

	private final ProductMapper productMapper;
	
	public List<ProductListResponse> getProductList(
	        String sort,
	        Integer menuSubId,
	        Timestamp lastCreatedAt,
	        Integer lastProductId,
	        Integer lastPopularity
	) {
	    List<ProductListResponse> productList =
	            productMapper.getProductList(sort, menuSubId, lastCreatedAt, lastProductId, lastPopularity);

	    if (productList.isEmpty()) return productList;

	    // ✅ 1) productId 목록 추출
	    List<Integer> productIds = productList.stream()
	            .map(ProductListResponse::getProductId)
	            .toList();

	    // ✅ 2) 이미지들을 "한 방"에 가져오기 (쿼리 1번)
	    List<ProductImageFile> allImages = productMapper.getProductImageListByProductIds(productIds);

	    // ✅ 3) productId로 그룹핑
	    Map<Integer, List<ProductImageFile>> imageMap = allImages.stream()
	            .collect(java.util.stream.Collectors.groupingBy(ProductImageFile::getProductId));

	    // ✅ 4) DTO에 주입 (없으면 빈 리스트)
	    for (ProductListResponse p : productList) {
	        p.setProductImageList(imageMap.getOrDefault(p.getProductId(), java.util.Collections.emptyList()));
	    }

	    return productList;
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

	public void addCart(Integer productOptionId, Integer quantity, Integer userNo) {
		productMapper.addCart(productOptionId, quantity, userNo);
	}
	

	public ProductDetailResponse getProductDetail(int productId) {
		ProductDetailResponse productDetail = productMapper.getProductDetail(productId);
		
		productDetail.setProductImageList(
	        productMapper.getProductImageList(productId)
	    );
		
		return productDetail;
	}

	public List<ProductOptionResponse> getProductOptionList(int productId) {
		return productMapper.getProductOptionList(productId);
	}

	public List<AvailableProductCouponResponse> getAvailableProductCoupon(int productId, Integer userNo) {
		return productMapper.getAvailableProductCoupon(productId, userNo);
	}
	
	public List<ProductReviewResponse> getProductReviewList(Integer productId, Integer userNo) {
		List<ProductReviewResponse> list = productMapper.getProductReviewList(productId, userNo);
		
		if (list == null || list.isEmpty()) return list;
		
		for (ProductReviewResponse review : list) {
	        Integer writerNo = review.getUserNo();
	        review.setUserNo(null);
	        boolean isOwner = userNo != null && writerNo != null && writerNo == userNo;

	        // 1) 작성자가 아니면 userId 마스킹(앞 2글자 + *****)
	        if (!isOwner) {
	        	review.setUserName(MaskingUtil.maskUserIdName(review.getUserName(), 5));
	        }
	    }

	    return list;
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


}
