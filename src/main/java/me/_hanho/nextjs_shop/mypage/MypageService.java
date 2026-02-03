package me._hanho.nextjs_shop.mypage;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import me._hanho.nextjs_shop.common.exception.BusinessException;
import me._hanho.nextjs_shop.common.exception.ErrorCode;

@Service
@RequiredArgsConstructor
public class MypageService {
	
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
	public List<CartProductResponse> getCartList(Integer userNo) {
		// 재고 부족한 얘들 선택 해제
		mypageMapper.unselectOutOfStockItems(userNo);
		//
		return mypageMapper.getCartList(userNo);
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
