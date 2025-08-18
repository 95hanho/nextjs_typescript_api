package me._hanho.nextjs_shop.mypage;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import me._hanho.nextjs_shop.model.Coupon;

@Service
public class MypageService {

	
	@Autowired
	private MypageMapper mypageMapper;

	public List<Coupon> getUserCoupons(String user_id) {
		return mypageMapper.getUserCoupons(user_id);
	}
}
