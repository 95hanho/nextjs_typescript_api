package me._hanho.nextjs_shop.admin;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import me._hanho.nextjs_shop.auth.AuthService;
import me._hanho.nextjs_shop.auth.TokenDTO;
import me._hanho.nextjs_shop.auth.TokenService;
import me._hanho.nextjs_shop.model.Token;
import me._hanho.nextjs_shop.seller.SellerRegisterRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bapi/admin")
public class AdminController {

	private static final Logger logger = LoggerFactory.getLogger(AdminController.class);
	
	private final AdminService adminService;
	private final TokenService tokenService;
	private final AuthService authService;
	
	// 암호화 비번 출력
	@PostMapping("/hash-password")
	public ResponseEntity<Map<String, Object>> getEncryptionPassword(@RequestParam("password") String password) {
		logger.info("getEncryptionPassword :" + password);
		Map<String, Object> result = new HashMap<String, Object>();
		
		String encryptionPassword = adminService.getEncryptionPassword(password);
		
		result.put("password", password);
		result.put("encryptionPassword", encryptionPassword);
		result.put("message", "SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	// 관리자 정보가져오기
	@GetMapping
	public ResponseEntity<Map<String, Object>> getAdminInfo(@RequestAttribute("adminNo") int adminNo) {
		logger.info("getAdminNoInfo : adminNo=" + adminNo);
		Map<String, Object> result = new HashMap<String, Object>();
		
		try {
			AdminInfo admin = adminService.getAdminInfo(adminNo);
			if(admin == null) {
				result.put("message", "ADMIN_NOT_FOUND"); // 존재하지 않는 사용자 (로그인 실패 때와 동일)
				return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
			} else {
				result.put("admin", admin);
				result.put("message", "ADMIN_FETCH_SUCCESS");
				return new ResponseEntity<>(result, HttpStatus.OK);
			}
		} catch(Exception e) {
			logger.error(e.getMessage());
			logger.error("token 제대로 안온듯");
			result.put("message", "UNAUTHORIZED_USER"); // 인증 실패로 조회 불가
			return new ResponseEntity<>(result, HttpStatus.UNAUTHORIZED); 
		}
	}
	// 로그인
	@PostMapping
	public ResponseEntity<Map<String, Object>> login(@ModelAttribute AdminLoginDTO adminLogin) {
		logger.info("adminLogin :" + adminLogin);
		Map<String, Object> result = new HashMap<String, Object>();
		
		AdminLoginDTO checkAdmin = adminService.isAdmin(adminLogin.getAdminId());
		if (checkAdmin == null || !adminService.passwordCheck(adminLogin.getPassword(), checkAdmin.getPassword())) {
			result.put("message", "SELLER_NOT_FOUND"); // 입력하신 아이디 또는 비밀번호가 일치하지 않습니다
			logger.error("입력하신 아이디 또는 비밀번호가 일치하지 않습니다");
			
			return new ResponseEntity<>(
					result
					, HttpStatus.UNAUTHORIZED);
		} else {
			// 마지막 로그인 표시
			adminService.updateLastLoginAt(checkAdmin.getAdminNo());
			
			result.put("adminNo", checkAdmin.getAdminNo());
			result.put("message", "LOGIN_SUCCESS");
			return new ResponseEntity<>(
					result
					, HttpStatus.OK);
		}
	}
	// 로그인 토큰 저장
	@PostMapping("/token")
	public ResponseEntity<Map<String, Object>> tokenStore(
			@RequestAttribute("adminNo") int adminNo, @RequestParam("refreshToken") String refreshToken,
			@RequestHeader("user-agent") String userAgent, @RequestHeader("x-forwarded-for") String forwardedFor) {
		logger.info("insertToken refreshToken : " + refreshToken.substring(refreshToken.length() - 10) + ", adminNo : " + adminNo + 
				", user-agent : " + userAgent + ", x-forwarded-for : " + forwardedFor);
		Map<String, Object> result = new HashMap<String, Object>();
		
		String ipAddress = forwardedFor != null ? forwardedFor : "unknown";
		Token token = Token.builder().connectIp(ipAddress).connectAgent(userAgent).refreshToken(refreshToken).adminNo(adminNo).build(); 
		adminService.insertToken(token);
		
		result.put("message", "ADMIN_TOKEN_INSERT_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	// 로그인 토큰 수정(재저장)
	@PostMapping("/token/refresh")
	public ResponseEntity<Map<String, Object>> updateToken(
			@RequestParam("beforeToken") String beforeToken , @RequestParam("refreshToken") String refreshToken,
			@RequestHeader("user-agent") String userAgent, @RequestHeader("x-forwarded-for") String forwardedFor) {
		logger.info("updateToken beforeToken : " + beforeToken.substring(beforeToken.length() - 10) + 
				", refreshToken : " + refreshToken.substring(refreshToken.length() - 10) + ", user-agent : " + userAgent + 
				", x-forwarded-for : " + forwardedFor);
		Map<String, Object> result = new HashMap<String, Object>();
		
		try {
			tokenService.parseJwtRefreshToken(beforeToken);
			tokenService.parseJwtRefreshToken(refreshToken);
		} catch (Exception e) {
            // 토큰이 유효하지 않으면 요청을 거부
        	logger.error("token UNAUTHORIZED");
        	return new ResponseEntity<>(result, HttpStatus.UNAUTHORIZED);
        }
		
		String ipAddress = forwardedFor != null ? forwardedFor : "unknown";
		TokenDTO token = TokenDTO.builder().connectIp(ipAddress).connectAgent(userAgent).refreshToken(refreshToken).beforeToken(beforeToken).build(); 
		authService.updateToken(token);
		
		String adminId = adminService.getAdminIdByToken(token);
		
		if(adminId == null) {
			result.put("message", "WRONG_TOKEN");
			return new ResponseEntity<>(result, HttpStatus.UNAUTHORIZED);
		}
		
		result.put("adminId", adminId);
		result.put("message", "TOKEN_UPDATE_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	// 판매자 추가
	@PostMapping("/seller")
	public ResponseEntity<Map<String, Object>> addSeller(@ModelAttribute SellerRegisterRequest seller,
			@RequestAttribute("adminNo") int adminNo) {
		logger.info("addSeller " + seller);
		Map<String, Object> result = new HashMap<String, Object>();

		// 판매자 중복 (이미 존재) 동일 판매자 존재로 추가 불가
		if(!adminService.hasSeller(seller.getSellerId())) {
			result.put("message", "SELLER_DUPLICATED");
			return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
		}
		
		// 예시 비번 : pass1234!
		adminService.addSeller(seller);
		
		/*
		 *
			판매자 추가 실패	SELLER_ADD_FAILED	등록 중 오류 발생
			권한 부족 (관리자만 가능)	UNAUTHORIZED_ADMIN	관리자 권한 없음
		 */

		result.put("message", "SELLER_ADD_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	// 판매자 승인여부 변경
	@PostMapping("/seller/approval")
	public ResponseEntity<Map<String, Object>> setSellerApproval(@ModelAttribute SellerApprovalRequest sellerApproval,
			@RequestAttribute("adminNo") Integer adminNo) {
		logger.info("setSellerApproval {} : " + sellerApproval);
		Map<String, Object> result = new HashMap<String, Object>();
		
		if ("APPROVED".equals(sellerApproval.getApprovalStatus()) && adminNo == null) {
		    result.put("message", "adminNo required");
			return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
		}
		
		sellerApproval.setApprovedBy(adminNo);
		adminService.setSellerApproval(sellerApproval);
		
		result.put("message", "SELLER_APPROVE_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
}
