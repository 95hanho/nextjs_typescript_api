package me._hanho.nextjs_shop.mypage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import me._hanho.nextjs_shop.common.exception.BusinessException;
import me._hanho.nextjs_shop.common.exception.ErrorCode;
import me._hanho.nextjs_shop.file.FileService;
import me._hanho.nextjs_shop.file.dto.FileUploadRequest;
import me._hanho.nextjs_shop.mypage.dto.AddReviewFileMeta;
import me._hanho.nextjs_shop.mypage.dto.AddReviewRequest;
import me._hanho.nextjs_shop.mypage.dto.AddUserAddressRequest;
import me._hanho.nextjs_shop.mypage.dto.AvailableCartCouponAtCartResponse;
import me._hanho.nextjs_shop.mypage.dto.AvailableSellerCouponAtCartResponse;
import me._hanho.nextjs_shop.mypage.dto.CartProductResponse;
import me._hanho.nextjs_shop.mypage.dto.CartSummaryResponse;
import me._hanho.nextjs_shop.mypage.dto.MyOrderDetailItem;
import me._hanho.nextjs_shop.mypage.dto.MyOrderDetailItemCoupon;
import me._hanho.nextjs_shop.mypage.dto.MyOrderDetailResponse;
import me._hanho.nextjs_shop.mypage.dto.MyOrderGroupResponse;
import me._hanho.nextjs_shop.mypage.dto.MyOrderItemResponse;
import me._hanho.nextjs_shop.mypage.dto.ReviewImageResponse;
import me._hanho.nextjs_shop.mypage.dto.ReviewOrderInfoResponse;
import me._hanho.nextjs_shop.mypage.dto.ReviewResponse;
import me._hanho.nextjs_shop.mypage.dto.SetReviewImageRequest;
import me._hanho.nextjs_shop.mypage.dto.UpdateCartRequest;
import me._hanho.nextjs_shop.mypage.dto.UpdateReviewRequest;
import me._hanho.nextjs_shop.mypage.dto.UpdateUserAddressRequest;
import me._hanho.nextjs_shop.mypage.dto.UserAddressResponse;
import me._hanho.nextjs_shop.mypage.dto.UserCouponResponse;
import me._hanho.nextjs_shop.mypage.dto.WishlistItemResponse;
import me._hanho.nextjs_shop.product.ProductMapper;
import me._hanho.nextjs_shop.product.dto.ProductImageFile;

@Service
@RequiredArgsConstructor
public class MypageService {
	
	private static final Logger logger = Logger.getLogger(MypageService.class.getName());

	private final MypageMapper mypageMapper;
	private final ProductMapper productMapper;
	private final FileService fileService;

	public List<UserCouponResponse> getUserCoupons(Integer userNo) {
		return mypageMapper.getUserCoupons(userNo);
	}
	@Transactional
	public List<MyOrderGroupResponse> getMyOrderList(Integer userNo, String keyword) {
		List<MyOrderGroupResponse> myOrderList = mypageMapper.getMyOrderListGroupList(userNo, keyword);

		List<Integer> orderIds = myOrderList.stream()
				.map(MyOrderGroupResponse::getOrderId)
				.toList();

		if(!orderIds.isEmpty()) {
			List<MyOrderItemResponse> allItems = mypageMapper.getMyOrderItemsByOrderIds(orderIds);
			for(MyOrderGroupResponse order : myOrderList) {
				List<MyOrderItemResponse> itemsForOrder = allItems.stream()
						.filter(item -> item.getOrderId() == order.getOrderId())
						.toList();
				order.setItems(itemsForOrder);
			}
		}



		return myOrderList;
	}
	//
	public MyOrderDetailResponse getMyOrderDetail(String orderId, Integer userNo) {
		return mypageMapper.getMyOrderDetail(orderId, userNo);
	}
	public List<MyOrderDetailItem> getMyOrderDetailItems(String orderId, Integer userNo) {
		List<MyOrderDetailItem> items = mypageMapper.getMyOrderDetailItems(orderId, userNo);
		List<Integer> orderItemIds = items.stream()
				.map(MyOrderDetailItem::getOrderItemId)
				.toList();
		List<MyOrderDetailItemCoupon> coupons = mypageMapper.getOrderItemCouponsByOrderItemIds(orderItemIds);
		for(MyOrderDetailItem item : items) {
			List<MyOrderDetailItemCoupon> couponsForItem = coupons.stream()
					.filter(coupon -> coupon.getOrderItemId() == item.getOrderItemId())
					.toList();
			item.setCoupons(couponsForItem);
		}
		return items;
	}

	public ReviewOrderInfoResponse getReviewOrderInfo(Integer orderItemId, Integer userNo) {
		ReviewOrderInfoResponse reviewOrderInfo = mypageMapper.getReviewOrderInfo(orderItemId, userNo);
		if(reviewOrderInfo == null) {
			throw new BusinessException(ErrorCode.REVIEW_ITEM_NOT_FOUND, "Order item not found for review: " + orderItemId);
		}
		return reviewOrderInfo;
	}
	public ReviewResponse getReviewByOrderItemId(Integer orderItemId, Integer userNo) {
		ReviewResponse review = mypageMapper.getReviewByOrderItemId(orderItemId, userNo);

		// 이미지 불러오기
		if(review != null) {
			List<ReviewImageResponse> reviewImages = mypageMapper.getReviewImagesByReviewId(review.getReviewId());
			review.setReviewImages(reviewImages);
		} else {
			logger.info("No review found for orderItemId: " + orderItemId + ", userNo: " + userNo);
		}

		return review;
	}

	public int insertReview(AddReviewRequest review, Integer userNo) {
		mypageMapper.insertReview(review, userNo);
		return review.getReviewId();
	}
	public void updateReview(UpdateReviewRequest review, Integer userNo) {
		mypageMapper.updateReview(review, userNo);
	}
	@Transactional
	public void setReviewImages(SetReviewImageRequest request, List<MultipartFile> files, Integer userNo) {
		// 1. 상품 소유자 검증
		// 2. 요청 유효성 검증
		// 3. 삭제 처리(nextjs_shop_file is_deleted = true)
		if(request.getDeleteImageIds().size() > 0) {
			mypageMapper.deleteReviewImages(request.getDeleteImageIds(), userNo);
		}
		// 4. 수정 처리
		if(request.getUpdateFiles().size() > 0) {
			mypageMapper.updateReviewImages(request.getUpdateFiles(), userNo);
		}
		// 5. 추가 처리
		Integer reviewId = request.getReviewId();
		List<AddReviewFileMeta> addFiles = request.getAddFiles();
		List<String> storeNames = new ArrayList<>(); // 업로드된 파일명 저장 (추가 처리 중 오류 발생 시, 업로드된 파일 삭제 위해)
		try {
			if (addFiles != null && !addFiles.isEmpty()) {
				if (files == null || addFiles.size() != files.size()) {
					throw new BusinessException(ErrorCode.BAD_REQUEST);
				}
				for (int i = 0; i < addFiles.size(); i++) {
					AddReviewFileMeta meta = addFiles.get(i);
					MultipartFile file = files.get(i);
					FileUploadRequest fileUploadRequest = fileService.fileUploadImage(file);
					storeNames.add(fileUploadRequest.getStoreName());
					meta.setFileId(fileUploadRequest.getFileId());
					mypageMapper.insertReviewImage(meta, reviewId, userNo);
				}
			}

		} catch (Exception e) {
			// 5-1. 추가 처리 중 오류 발생 시, 업로드된 파일들 삭제
			for (String storeName : storeNames) {
				try {
					fileService.deleteFile(storeName);
				} catch (Exception deleteEx) {
					// 로그만 남기기
				}
			}
			throw e;
		}
		// 6. 썸네일/정렬 최종 보정


	}
	@Transactional
	public CartSummaryResponse getCartSummary(Integer userNo) {
		// 장바구니 조회 시 점유한 상품이 있으면 해제
		mypageMapper.releaseHoldIfExists(userNo);

		// Must : 판매 중지된 상품 삭제, 판매자가 정지계정인 상품 삭제
		
		CartSummaryResponse cartSummary = new CartSummaryResponse();
		// 재고 부족한 얘들 선택 해제
		int updated = mypageMapper.unselectOutOfStockItems(userNo);
		System.out.println("updated: " + updated);
		if (updated > 0) {
			logger.info("재고 부족한 제품 선택 해제: " + updated);
		}
		List<CartProductResponse> cartList = mypageMapper.getCartList(userNo);

		if(cartList.isEmpty()) {
			throw new BusinessException(ErrorCode.CART_EMPTY, "Cart is empty for userNo: " + userNo);
		}

		// 장바구니에 담긴 제품들의 Id 조회
		List<Integer> productIds = cartList.stream()
		.map(CartProductResponse::getProductId)
		.distinct()
		.toList();
		
		cartSummary.setCartList(cartList);
		cartSummary.setProductIds(productIds);
		cartSummary.setExceedQuantity(updated > 0);
		return cartSummary;
	}
	public List<AvailableCartCouponAtCartResponse> getAvailableCartCouponsAtCart(List<Integer> productIds, Integer userNo) {
		return mypageMapper.getAvailableCartCouponsAtCart(productIds, userNo);
	}

	public List<AvailableSellerCouponAtCartResponse> getAvailableSellerCouponsAtCart(List<Integer> productIds, Integer userNo) {
		return mypageMapper.getAvailableSellerCouponsAtCart(productIds, userNo);
	}

	public void updateCart(UpdateCartRequest cart, Integer userNo) {
	    int updated = mypageMapper.updateCart(cart, userNo);
	    if (updated == 0) {
	        throw new BusinessException(ErrorCode.CART_NOT_FOUND, "Cart not found: " + cart.getCartId());
	    }
	}
	public void updateSelectedCart(List<Integer> cartIdList, Boolean selected, Integer userNo) {
	    int updated = mypageMapper.updateSelectedCart(cartIdList, selected, userNo);
	    if (updated == 0) {
	        throw new BusinessException(ErrorCode.CART_NOT_FOUND, "Cart not found: " + cartIdList);
	    }
	}
	public void deleteCart(List<Integer> cartIdList, Integer userNo) {
	    int updated = mypageMapper.deleteCart(cartIdList, userNo);
	    if (updated == 0) {
	        throw new BusinessException(ErrorCode.CART_NOT_FOUND, "Cart not found: " + cartIdList);
	    }
	}
	public List<WishlistItemResponse> getWishlistItems(Integer userNo) {
		List<WishlistItemResponse> wishlistItems = mypageMapper.getWishlistItems(userNo);

		if (wishlistItems.isEmpty()) return wishlistItems;

		// ✅ 1) productId 목록 추출
		List<Integer> productIds = wishlistItems.stream()
	            .map(WishlistItemResponse::getProductId)
	            .toList();

		// ✅ 2) 이미지들을 "한 방"에 가져오기 (쿼리 1번)
	    List<ProductImageFile> allImages = productMapper.getProductImageListByProductIds(productIds);

		// ✅ 3) productId로 그룹핑
	    Map<Integer, List<ProductImageFile>> imageMap = allImages.stream()
	            .collect(java.util.stream.Collectors.groupingBy(ProductImageFile::getProductId));

		// ✅ 4) DTO에 주입 (없으면 빈 리스트)
	    for (WishlistItemResponse p : wishlistItems) {
	        p.setProductImageList(imageMap.getOrDefault(p.getProductId(), java.util.Collections.emptyList()));
	    }

		return wishlistItems;
	}

	public List<UserAddressResponse> getUserAddressList(Integer userNo) {
		return mypageMapper.getUserAddressList(userNo);
	}
	public void insertUserAddress(AddUserAddressRequest userAddress, Integer userNo) {
		mypageMapper.insertUserAddress(userAddress, userNo);
	}
	@Transactional
	public void updateUserAddress(UpdateUserAddressRequest userAddress, Integer userNo) {
	    if (userAddress.getDefaultAddress()) {
	        mypageMapper.clearDefaultAddress(userAddress.getAddressId(), userNo);
	    }
	    int updated = mypageMapper.updateUserAddress(userAddress, userNo);
	    if (updated == 0) {
	        throw new BusinessException(ErrorCode.ADDRESS_NOT_FOUND, "Address not found: " + userAddress.getAddressId());
	    }
	}
	@Transactional
	public void deleteUserAddress(Integer addressId, Integer userNo) {
		int isDefault = mypageMapper.isDefaultAddress(addressId, userNo);
		int updated = mypageMapper.deleteUserAddress(addressId, userNo);
	    if (updated == 0) {
	        throw new BusinessException(ErrorCode.ADDRESS_NOT_FOUND, "Address not found: " + addressId);
	    }
		if(isDefault == 1) {
			mypageMapper.updateDefaultLatest(userNo);
		}
	}

}
