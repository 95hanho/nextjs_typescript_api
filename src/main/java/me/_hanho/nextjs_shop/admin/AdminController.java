package me._hanho.nextjs_shop.admin;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import me._hanho.nextjs_shop.model.Seller;

@RestController
@RequestMapping("/bapi/admin")
public class AdminController {

	private static final Logger logger = LoggerFactory.getLogger(AdminController.class);
	
	@Autowired
	private AdminService adminService;
	
	// 판매자 추가
	@PostMapping("/seller")
	public ResponseEntity<Map<String, Object>> addSeller(@ModelAttribute Seller seller) {
		logger.info("addSeller " + seller);
		Map<String, Object> result = new HashMap<String, Object>();

		// 예시 비번 : pass1234!
		adminService.addSeller(seller);
		
		/*
		 *
			판매자 추가 실패	SELLER_ADD_FAILED	등록 중 오류 발생
			판매자 중복 (이미 존재)	SELLER_DUPLICATED	동일 판매자 존재로 추가 불가
			권한 부족 (관리자만 가능)	UNAUTHORIZED_ADMIN	관리자 권한 없음
		 */

		result.put("message", "SELLER_ADD_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
}
