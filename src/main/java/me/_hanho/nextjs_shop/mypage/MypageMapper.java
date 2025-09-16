package me._hanho.nextjs_shop.mypage;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import me._hanho.nextjs_shop.model.Cart;
import me._hanho.nextjs_shop.model.Coupon;

@Mapper
public interface MypageMapper {

	List<UserCouponDTO> getUserCoupons(String user_id);

	List<MyOrderWithReviewDTO> getMyOrderWithReview(String user_id);
	
	List<CartProductDTO> getCartList(String user_id);
	
	int updateCart(Cart cart);
	
	int deleteCart(int cart_id);
	
	
	
	
	
	
	

	List<WishlistItemDTO> getWishlistItems(String user_id);

	void deleteWish(int wish_id);
	
	
}
