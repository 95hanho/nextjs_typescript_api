package me._hanho.nextjs_shop.mypage;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import me._hanho.nextjs_shop.auth.UserNotFoundException;
import me._hanho.nextjs_shop.model.Review;
import me._hanho.nextjs_shop.model.UserAddress;

@Service
@RequiredArgsConstructor
public class MypageService {

	
	private final MypageMapper mypageMapper;

	public List<UserCouponDTO> getUserCoupons(Integer userNo) {
		return mypageMapper.getUserCoupons(userNo);
	}
	public List<MyOrderGroupDTO> getMyOrderListWithReview(Integer userNo) {
		List<MyOrderGroupDTO> myOrderList = mypageMapper.getMyOrderListGroupList(userNo);
		//
		myOrderList.forEach(v ->
			v.setItems(mypageMapper.getMyOrderListProductWithReview(v.getOrderId()))
		);
		
		return myOrderList;
	}
	public MyOrderDetailDTO getMyOrderDetail(String orderId, Integer userNo) {
		MyOrderDetailDTO myOrderDetail = mypageMapper.getMyOrderDetail(orderId, userNo);
		myOrderDetail.setItems(mypageMapper.getMyOrderDetailItems(orderId, userNo));
		return myOrderDetail;
	}
	public void insertReview(Review review, Integer userNo) {
		mypageMapper.insertReview(review, userNo);
	}
	public List<CartProductDTO> getCartList(Integer userNo) {
		// 재고 부족한 얘들 선택 해제
		mypageMapper.unselectOutOfStockItems(userNo);
		//
		return mypageMapper.getCartList(userNo);
	}
	public void updateCart(UpdateCartRequest cart) {
		int updated = mypageMapper.updateCart(cart);
	    if (updated == 0) {
	        throw new UserNotFoundException("updateCart not found: " + cart.getCartId());
	    }
	}
	public void updateSelectedCart(UpdateSelectedCartDTO selectedCart) {
		int updated = mypageMapper.updateSelectedCart(selectedCart);
	    if (updated == 0) {
	        throw new UserNotFoundException("updateSelectedCart not found: " + selectedCart.getCartIdList().toString());
	    }
	}
	public void deleteCart(List<Integer> cartId, Integer userNo) {
		int updated = mypageMapper.deleteCart(cartId, userNo);
	    if (updated == 0) {
	        throw new UserNotFoundException("deleteCart not found: " + cartId);
	    }
	}
	public List<WishlistItemDTO> getWishlistItems(Integer userNo) {
		return mypageMapper.getWishlistItems(userNo);
	}

	public List<UserAddress> getUserAddressList(Integer userNo) {
		return mypageMapper.getUserAddressList(userNo);
	}
	public void insertUserAddress(AddUserAddressRequest userAddress) {
		mypageMapper.insertUserAddress(userAddress);
	}
	@Transactional
	public void updateUserAddress(UpdateUserAddressRequest userAddress) {
		if(userAddress.isDefaultAddress()) {
			mypageMapper.clearDefaultAddress(userAddress.getAddressId(), userAddress.getUserNo());
		}
		int updated = mypageMapper.updateUserAddress(userAddress);
		if (updated == 0) {
			throw new UserNotFoundException("updateUserAddress not found: " + userAddress.getAddressId());
	    }
	}

	public void deleteUserAddress(int addressId, Integer userNo) {
		int updated = mypageMapper.deleteUserAddress(addressId, userNo);
		if(updated == 0) {
			throw new UserNotFoundException("deleteUserAddress not found: " + addressId);
		}
		
	}








}
