package me._hanho.nextjs_shop.auth;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import me._hanho.nextjs_shop.model.Token;
import me._hanho.nextjs_shop.model.User;

@RestController
@RequestMapping("/bapi/auth")
public class AuthController {
	
	private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

	@Autowired
	private AuthService authService;
	
//	@Autowired
//	private TokenService tokenService;
	
	// 유저정보가져오기
	@GetMapping
	public ResponseEntity<Map<String, Object>> getUserInfo(@RequestParam("userId") String userId) {
		logger.info("getUserInfo : userId=" + userId);
		Map<String, Object> result = new HashMap<String, Object>();
		
		try {
			User user = authService.getUserExceptPassword(userId);
			if(user == null) {
				result.put("message", "USER_NOT_FOUND"); // 존재하지 않는 사용자 (로그인 실패 때와 동일)
				return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
			} else {
				result.put("user", user);
				result.put("message", "USER_FETCH_SUCCESS");
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
	public ResponseEntity<Map<String, Object>> login(@ModelAttribute User user, @RequestHeader("User-Agent") String agent
			, HttpServletRequest request) {
		logger.info("login :" + user);
		Map<String, Object> result = new HashMap<String, Object>();
		
		User checkUser = authService.getUser(user.getUserId());
		if (checkUser == null || !authService.passwordCheck(user.getPassword(), checkUser.getPassword())) {
			result.put("message", "USER_NOT_FOUND"); // 입력하신 아이디 또는 비밀번호가 일치하지 않습니다
			logger.error("입력하신 아이디 또는 비밀번호가 일치하지 않습니다");
			
			return new ResponseEntity<>(
					result
					, HttpStatus.UNAUTHORIZED);
		} else {
			 // bbf로 변경하면서 필요없음
//			User onlyId = new User();
//			onlyId.setUserId(checkUser.getUserId());
//			String accessToken = tokenService.makeJwtToken(600, onlyId);
//			String refreshToken = tokenService.makeJwtToken(1800);
//			String ipAddress = request.getRemoteAddr();
//			Token token = Token.builder().connectIp(ipAddress).connectAgent(agent).refreshToken(refreshToken).userId(checkUser.getUserId()).build();
//			authService.insertToken(token);
			
			
//			result.put("accessToken", accessToken);
//			result.put("refreshToken", refreshToken);
			
			result.put("message", "LOGIN_SUCCESS");
			return new ResponseEntity<>(
					result
					, HttpStatus.OK);
		}
	}
	
	// 아이디 중복확인 
	@GetMapping("/id")
	public ResponseEntity<Map<String, Object>> idDuplcheck(@RequestParam("userId") String userId) {
		logger.info("idDuplcheck userId : " + userId);
		Map<String, Object> result = new HashMap<String, Object>();
		
		boolean hasId = authService.getId(userId);
		logger.info("hasId : " + hasId);
		
		if(!hasId) {
			result.put("message", "ID_AVAILABLE");
			return new ResponseEntity<>(result, HttpStatus.OK);
		} else {
			result.put("message", "ID_DUPLICATED");
			return new ResponseEntity<>(result, HttpStatus.CONFLICT);
		}
	}
	// 휴대폰인증
	@PostMapping("/phone")
	public ResponseEntity<Map<String, Object>> phoneAuth(@RequestParam("phone") String phone) {
		logger.info("phoneAuth");
		Map<String, Object> result = new HashMap<String, Object>();
		
		// "INVALID_VERIFICATION_CODE" 인증번호가 올바르지 않음
		// "VERIFICATION_EXPIRED" 인증번호 유효기간 만료
		// "VERIFICATION_SENT" 인증번호 전송 완료
		
		result.put("message", "PHONE_VERIFIED");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	// 회원가입
	@PostMapping("/user")
	public ResponseEntity<Map<String, Object>> join(@ModelAttribute User user) {
		logger.info("join : " + user);
		Map<String, Object> result = new HashMap<String, Object>();
		
//		authService.joinUser(user);
		
		result.put("message", "JOIN_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	// 회원 정보 변경
	@PutMapping("/user")
	public ResponseEntity<Map<String, Object>> userInfoUpdate(@ModelAttribute User user) {
		logger.info("userInfoUpdate : 회원 정보 변경 - " + user);
		Map<String, Object> result = new HashMap<String, Object>();
		
		authService.userInfoUpdate(user);
		
//		"USER_UPDATE_FAILED" : 변경 중 오류 발생 (DB 문제 등)
//		"UNAUTHORIZED_USER" : 권한이 없음.
		
		result.put("message", "USER_UPDATE_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	// 로그인 토큰 저장
	@PostMapping("/token")
	public ResponseEntity<Map<String, Object>> insertToken(
			@RequestParam("refreshToken") String refreshToken, @RequestParam("userId") String userId,
			@RequestHeader("user-agent") String userAgent, @RequestHeader("x-forwarded-for") String forwardedFor) {
		logger.info("insertToken refreshToken : " + refreshToken + ", user-agent : " + userAgent + ", x-forwarded-for : " + forwardedFor);
		Map<String, Object> result = new HashMap<String, Object>();
		
		String ipAddress = forwardedFor != null ? forwardedFor : "unknown";
		Token token = Token.builder().connectIp(ipAddress).connectAgent(userAgent).refreshToken(refreshToken).userId(userId).build(); 
		authService.insertToken(token);
		
		result.put("message", "TOKEN_INSERT_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	// 로그인 토큰 수정(재저장)
	@PutMapping("/token")
	public ResponseEntity<Map<String, Object>> updateToken(
			@RequestParam("beforeToken") String beforeToken , @RequestParam("refreshToken") String refreshToken,
			@RequestHeader("user-agent") String userAgent, @RequestHeader("x-forwarded-for") String forwardedFor) {
		logger.info("updateToken refreshToken : " + refreshToken + ", user-agent : " + userAgent + ", x-forwarded-for : " + forwardedFor);
		Map<String, Object> result = new HashMap<String, Object>();
		
		String ipAddress = forwardedFor != null ? forwardedFor : "unknown";
		TokenDTO token = TokenDTO.builder().connectIp(ipAddress).connectAgent(userAgent).refreshToken(beforeToken).beforeToken(beforeToken).build(); 
		authService.updateToken(token);
		
		String userId = authService.getUserIdByToken(token);
		
		result.put("userId", userId);
		result.put("message", "TOKEN_UPDATE_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	
}
