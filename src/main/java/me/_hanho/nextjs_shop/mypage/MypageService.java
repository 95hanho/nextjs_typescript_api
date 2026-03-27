package me._hanho.nextjs_shop.mypage;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import me._hanho.nextjs_shop.common.exception.BusinessException;
import me._hanho.nextjs_shop.common.exception.ErrorCode;
import me._hanho.nextjs_shop.mypage.dto.AddReviewRequest;
import me._hanho.nextjs_shop.mypage.dto.AddUserAddressRequest;
import me._hanho.nextjs_shop.mypage.dto.AvailableCartCouponAtCartResponse;
import me._hanho.nextjs_shop.mypage.dto.AvailableSellerCouponAtCartResponse;
import me._hanho.nextjs_shop.mypage.dto.CartProductResponse;
import me._hanho.nextjs_shop.mypage.dto.CartSummaryResponse;
import me._hanho.nextjs_shop.mypage.dto.MyOrderDetailResponse;
import me._hanho.nextjs_shop.mypage.dto.MyOrderGroupResponse;
import me._hanho.nextjs_shop.mypage.dto.UpdateCartRequest;
import me._hanho.nextjs_shop.mypage.dto.UpdateUserAddressRequest;
import me._hanho.nextjs_shop.mypage.dto.UserAddressResponse;
import me._hanho.nextjs_shop.mypage.dto.UserCouponResponse;
import me._hanho.nextjs_shop.mypage.dto.WishlistItemResponse;

@Service
@RequiredArgsConstructor
public class MypageService {
	
	private static final Logger logger = Logger.getLogger(MypageService.class.getName());

	private final MypageMapper mypageMapper;

	public List<UserCouponResponse> getUserCoupons(Integer userNo) {
		return mypageMapper.getUserCoupons(userNo);
	}
	@Transactional
	public List<MyOrderGroupResponse> getMyOrderListWithReview(Integer userNo) {
		List<MyOrderGroupResponse> myOrderList = mypageMapper.getMyOrderListGroupList(userNo);
		//
		myOrderList.forEach(v ->
			v.setItems(mypageMapper.getMyOrderListProductWithReview(v.getOrderId()))
		);
		
		return myOrderList;
	}
	@Transactional
	public MyOrderDetailResponse getMyOrderDetail(String orderId, Integer userNo) {
		MyOrderDetailResponse myOrderDetail = mypageMapper.getMyOrderDetail(orderId, userNo);
		myOrderDetail.setItems(mypageMapper.getMyOrderDetailItems(orderId, userNo));
		return myOrderDetail;
	}
	public void insertReview(AddReviewRequest review, Integer userNo) {
		mypageMapper.insertReview(review, userNo);
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
		return mypageMapper.getWishlistItems(userNo);
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
