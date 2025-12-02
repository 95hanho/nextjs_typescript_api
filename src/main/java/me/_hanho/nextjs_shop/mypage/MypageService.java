package me._hanho.nextjs_shop.mypage;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import me._hanho.nextjs_shop.auth.UserNotFoundException;
import me._hanho.nextjs_shop.model.Cart;
import me._hanho.nextjs_shop.model.Review;
import me._hanho.nextjs_shop.model.UserAddress;

@Service
public class MypageService {

	
	@Autowired
	private MypageMapper mypageMapper;

	public List<UserCouponDTO> getUserCoupons(String userId) {
		return mypageMapper.getUserCoupons(userId);
	}
	public List<MyOrderGroupDTO> getMyOrderListWithReview(String userId) {
		List<MyOrderGroupDTO> myOrderList = mypageMapper.getMyOrderListGroupList(userId);
		//
		myOrderList.forEach(v ->
			v.setItems(mypageMapper.getMyOrderListProductWithReview(v.getOrderId()))
		);
		
		return myOrderList;
	}
	public MyOrderDetailDTO getMyOrderDetail(String orderId) {
		MyOrderDetailDTO myOrderDetail = mypageMapper.getMyOrderDetail(orderId);
		myOrderDetail.setItems(mypageMapper.getMyOrderDetailItems(orderId));
		return myOrderDetail;
	}
	public void insertReview(Review review) {
		mypageMapper.insertReview(review);
	}
	public List<CartProductDTO> getCartList(String userId) {
		return mypageMapper.getCartList(userId);
	}
	public void updateCart(Cart cart) {
		int updated = mypageMapper.updateCart(cart);
	    if (updated == 0) {
	        throw new UserNotFoundException("updateCart not found: " + cart.getCartId());
	    }
	}
	public void deleteCart(int cartId) {
		int updated = mypageMapper.deleteCart(cartId);
	    if (updated == 0) {
	        throw new UserNotFoundException("deleteCart not found: " + cartId);
	    }
	}
	public List<WishlistItemDTO> getWishlistItems(String userId) {
		return mypageMapper.getWishlistItems(userId);
	}

	public List<UserAddress> getUserAddressList(String userId) {
		return mypageMapper.getUserAddressList(userId);
	}
	public void insertUserAddress(UserAddress userAddress) {
		mypageMapper.insertUserAddress(userAddress);
	}

	public void updateUserAddress(UserAddress userAddress) {
		int updated = mypageMapper.updateUserAddress(userAddress);
		if (updated == 0) {
			throw new UserNotFoundException("updateUserAddress not found: " + userAddress.getAddressId());
	    }
		
	}

	public void deleteUserAddress(int addressId) {
		int updated = mypageMapper.deleteUserAddress(addressId);
		if(updated == 0) {
			throw new UserNotFoundException("deleteUserAddress not found: " + addressId);
		}
		
	}







}
