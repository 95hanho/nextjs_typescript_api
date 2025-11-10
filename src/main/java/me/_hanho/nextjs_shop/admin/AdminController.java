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

		result.put("message", "success");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
}
