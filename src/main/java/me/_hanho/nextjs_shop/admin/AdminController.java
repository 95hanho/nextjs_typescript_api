package me._hanho.nextjs_shop.admin;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import me._hanho.nextjs_shop.model.Token;
import me._hanho.nextjs_shop.seller.SellerRegisterRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bapi/admin")
public class AdminController {

	private static final Logger logger = LoggerFactory.getLogger(AdminController.class);
	
	private final AdminService adminService;
	
	// 로그인
	@PostMapping
	public ResponseEntity<Map<String, Object>> login(@ModelAttribute AdminLoginDTO adminLogin) {
		logger.info("adminLogin :" + adminLogin);
		Map<String, Object> result = new HashMap<String, Object>();
		
		AdminLoginDTO checkAdmin = adminService.isAdmin(adminLogin.getLoginId());
		if (checkAdmin == null || !adminService.passwordCheck(adminLogin.getPassword(), checkAdmin.getPassword())) {
			result.put("message", "SELLER_NOT_FOUND"); // 입력하신 아이디 또는 비밀번호가 일치하지 않습니다
			logger.error("입력하신 아이디 또는 비밀번호가 일치하지 않습니다");
			
			return new ResponseEntity<>(
					result
					, HttpStatus.UNAUTHORIZED);
		} else {
			result.put("message", "LOGIN_SUCCESS");
			return new ResponseEntity<>(
					result
					, HttpStatus.OK);
		}
	}
	// 로그인 토큰 저장
	@PostMapping("/token")
	public ResponseEntity<Map<String, Object>> tokenStore(
			@RequestAttribute("adminId") String adminId, @RequestParam("refreshToken") String refreshToken,
			@RequestHeader("user-agent") String userAgent, @RequestHeader("x-forwarded-for") String forwardedFor) {
//		logger.info("insertToken refreshToken : " + refreshToken.substring(refreshToken.length() - 10) + ", sellerId : " + sellerId + 
//				", user-agent : " + userAgent + ", x-forwarded-for : " + forwardedFor);
		Map<String, Object> result = new HashMap<String, Object>();
//		
//		String ipAddress = forwardedFor != null ? forwardedFor : "unknown";
//		Token token = Token.builder().connectIp(ipAddress).connectAgent(userAgent).refreshToken(refreshToken).sellerId(sellerId).build(); 
//		sellerService.insertToken(token);
//		
		result.put("message", "SELLER_TOKEN_INSERT_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	// 판매자 추가
	@PostMapping("/seller")
	public ResponseEntity<Map<String, Object>> addSeller(@ModelAttribute SellerRegisterRequest seller) {
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
	// 판매자 승인여부 변경
	@PostMapping("/seller/approval")
	public ResponseEntity<Map<String, Object>> setSellerApproval(@ModelAttribute SellerApprovalRequest sellerApproval) {
		logger.info("setSellerApproval {} : " + sellerApproval);
		Map<String, Object> result = new HashMap<String, Object>();

		adminService.setSellerApproval(sellerApproval);
		
		result.put("message", "SELLER_APPROVE_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
}
