package me._hanho.nextjs_shop.mypage;

import java.util.List;

import me._hanho.nextjs_shop.model.Coupon;

public interface MypageMapper {

	List<Coupon> getUserCoupons(String user_id);
	
}
