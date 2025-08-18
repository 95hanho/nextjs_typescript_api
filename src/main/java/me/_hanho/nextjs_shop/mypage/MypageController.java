package me._hanho.nextjs_shop.mypage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import me._hanho.nextjs_shop.model.Coupon;

@RestController
@RequestMapping("/bapi/mypage")
public class MypageController {

	private static final Logger logger = LoggerFactory.getLogger(MypageController.class);
	
	@Autowired
	private MypageService mypageService;
	
	// 유저 쿠폰 가져오기
	@GetMapping("/coupon")
	public ResponseEntity<Map<String, Object>> getUserCoupons(@RequestParam("user_id") String user_id) {
		logger.info("getMenus");
		Map<String, Object> result = new HashMap<String, Object>();
		
		List<Coupon> coupon_list = mypageService.getUserCoupons(user_id);
		
		result.put("coupon_list", coupon_list);
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
}
