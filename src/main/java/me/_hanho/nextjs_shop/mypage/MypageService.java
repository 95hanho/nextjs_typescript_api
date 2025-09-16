package me._hanho.nextjs_shop.mypage;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import me._hanho.nextjs_shop.auth.UserNotFoundException;
import me._hanho.nextjs_shop.model.Cart;
import me._hanho.nextjs_shop.model.Coupon;

@Service
public class MypageService {

	
	@Autowired
	private MypageMapper mypageMapper;

	public List<UserCouponDTO> getUserCoupons(String user_id) {
		return mypageMapper.getUserCoupons(user_id);
	}

	public List<MyOrderWithReviewDTO> getMyOrderWithReview(String user_id) {
		return mypageMapper.getMyOrderWithReview(user_id);
	}
	public List<CartProductDTO> getCartList(String user_id) {
		return mypageMapper.getCartList(user_id);
	}
	public void updateCart(Cart cart) {
		int updated = mypageMapper.updateCart(cart);
	    if (updated == 0) {
	        throw new UserNotFoundException("updateCart not found: " + cart.getCart_id());
	    }
	}
	public void deleteCart(int cart_id) {
		int updated = mypageMapper.deleteCart(cart_id);
	    if (updated == 0) {
	        throw new UserNotFoundException("deleteCart not found: " + cart_id);
	    }
	}
	public List<WishlistItemDTO> getWishlistItems(String user_id) {
		return mypageMapper.getWishlistItems(user_id);
	}

	public void deleteWish(int wish_id) {
		mypageMapper.deleteWish(wish_id);
	}





}
