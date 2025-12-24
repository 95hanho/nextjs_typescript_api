package me._hanho.nextjs_shop.auth;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import me._hanho.nextjs_shop.model.PhoneAuth;
import me._hanho.nextjs_shop.model.Token;
import me._hanho.nextjs_shop.model.User;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bapi/auth")
public class AuthController {
	
	private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

	private final AuthService authService;
    private final TokenService tokenService;
    private final PhoneAuthCodeService phoneAuthCodeService;

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
	public ResponseEntity<Map<String, Object>> sendPhoneAuth(@RequestParam("phone") String phone, @RequestParam("phoneAuthToken") String phoneAuthToken,
			@RequestParam(required = false, name = "userId") String userId,
			@RequestHeader("user-agent") String userAgent, @RequestHeader("x-forwarded-for") String forwardedFor) {
		logger.info("phoneAuth - phone : " + phone + ", userId : " + userId);
		Map<String, Object> result = new HashMap<String, Object>();
		String ipAddress = forwardedFor != null ? forwardedFor : "unknown";
		
		String phoneUserId = null;
		try {
			// JWT 파싱 및 복호화
	        Claims claims = tokenService.parseJwtPhoneAuthToken(phoneAuthToken);
	        // 휴대폰인증토큰 userId 추출
	        phoneUserId = claims.get("userId", String.class);
	        System.out.println("phoneUserId : " + phoneUserId);
		} catch(Exception e) {
			result.put("message", "VERIFICATION_EXPIRED");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
		}

		// 인증번호(6자리?)를 생성
		String verificationCode = phoneAuthCodeService.generate6DigitCode();
		logger.info("phoneAuth-verificationCode : " + verificationCode);
		
        // 토큰을 휴대폰인증DB에 (id, userId, phoneAuthToken, phone, verificationCode) 형태로 저장
		PhoneAuth phoneAuth = PhoneAuth.builder().userId(phoneUserId).phoneAuthToken(phoneAuthToken)
				.phone(phone).verificationCode(verificationCode).connectIp(ipAddress).connectAgent(userAgent).build();
		authService.insertPhoneAuth(phoneAuth);
		
		// 인증번호를 휴대폰으로 보냄!!
		
		result.put("message", "VERIFICATION_SENT");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	// 휴대폰인증 확인!!
	@PostMapping("/phone/check")
	public ResponseEntity<Map<String, Object>> phoneAuthCheck(
			@RequestParam("authNumber") String authNumber, 
			@RequestParam("phoneAuthToken") String phoneAuthToken,
			@RequestParam(required = false, name = "userId") String userId) {
		logger.info("phoneAuthCheck - authNumber='" + authNumber + ", userId : " + userId);
		Map<String, Object> result = new HashMap<String, Object>();
		
		String phoneUserId = null;
		try {
			// JWT 파싱 및 복호화
	        Claims claims = tokenService.parseJwtPhoneAuthToken(phoneAuthToken);
	        // 토큰의 userId 추출
	        phoneUserId = claims.get("userId", String.class);
	        System.out.println("phoneUserId : " + phoneUserId);
		} catch(Exception e) {
			result.put("message", "VERIFICATION_EXPIRED");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
		}
		
		PhoneAuth phoneAuth = authService.getPhoneAuthCode(phoneAuthToken);
		if (phoneAuth == null) {
			result.put("message", "VERIFICATION_EXPIRED");
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
	    }
		// 인증번호가 올바르지 않음
		if(!authNumber.equals(phoneAuth.getVerificationCode())) {
			result.put("message", "INVALID_VERIFICATION_CODE");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
		}
		// 인증번호 검증 성공(사용토큰으로 변경)
		authService.markPhoneAuthUsed(phoneAuth.getPhoneAuthId());
        
    	String userIdOfToken = phoneAuth.getUserId();
        if(userId == null) {
        	// 1. userId가 없고, phoneUserId에 id도 없다. = 회원가입
        	if(userIdOfToken == null) {
        		result.put("message", "PHONEAUTH_VALIDATE"); // 인증 성공
    			return new ResponseEntity<>(result, HttpStatus.OK);
        	} 
        	// 2. userId가 없고, phoneUserId가 존재하는 유저이다.  = 아이디찾기
        	else {
        		result.put("userId", userIdOfToken);
        		result.put("message", "IDFIND_SUCCESS"); // 인증 성공
        		return new ResponseEntity<>(result, HttpStatus.OK);
        	}
        } 
        // 3. userId가 있고, phoneUserId와 일치한다. = 비밀번호 찾기
        else if(userId != null && userId.equals(phoneUserId) && phoneUserId.equals(userIdOfToken)) {
        	result.put("userId", userIdOfToken);
        	result.put("message", "PWDFIND_SUCCESS"); // 인증 성공
    		return new ResponseEntity<>(result, HttpStatus.OK);
        }
        
        result.put("message", "INVALID_REQUEST");
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(result);
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
	// 비밀번호 변경
	@PostMapping("/password")
	public ResponseEntity<Map<String, Object>> passwordChange(@ModelAttribute PasswordChangeDTO pwdChangeDTO) {
		Map<String, Object> result = new HashMap<String, Object>();
		String userId;
		try {
			String pwdResetToken = pwdChangeDTO.getPwdResetToken();
			// JWT 파싱 및 복호화
	        Claims claims = tokenService.parseJwtPwdChangeToken(pwdResetToken);
	        // 토큰의 userId 추출
	        userId = claims.get("userId", String.class);
		} catch(Exception e) {
			result.put("message", "VERIFICATION_EXPIRED");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
		}
		
		// 현재 비밀번호가 있다면 비밀번호 확인(없으면 비밀번호찾기에서 바꾸는거)
		String curPassword = pwdChangeDTO.getCurPassword();
		if(curPassword != null) {
			User checkUser = authService.getUser(userId);
			if(!authService.passwordCheck(curPassword, checkUser.getPassword())) {
				result.put("message", "CURRENT_PASSWORD_MISMATCH");
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
			}
			if(authService.passwordCheck(pwdChangeDTO.getNewPassword(), checkUser.getPassword())) {
				result.put("message", "CURRENT_PASSWORD_EQUAL");
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
			}
		}
		
		// 비밀번호 변경
		authService.changePassword(userId, pwdChangeDTO.getNewPassword());
		
		result.put("message", "PASSWORD_CHANGE_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	// 로그인 토큰 저장
	@PostMapping("/token")
	public ResponseEntity<Map<String, Object>> insertToken(
			@RequestAttribute("userId") String userId, @RequestParam("refreshToken") String refreshToken,
			@RequestHeader("user-agent") String userAgent, @RequestHeader("x-forwarded-for") String forwardedFor) {
		logger.info("insertToken refreshToken : " + refreshToken.substring(refreshToken.length() - 10) + ", userId : " + userId + 
				", user-agent : " + userAgent + ", x-forwarded-for : " + forwardedFor);
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
		logger.info("updateToken beforeToken : " + beforeToken.substring(beforeToken.length() - 10) + 
				", refreshToken : " + refreshToken.substring(refreshToken.length() - 10) + ", user-agent : " + userAgent + 
				", x-forwarded-for : " + forwardedFor);
		Map<String, Object> result = new HashMap<String, Object>();
		
		String ipAddress = forwardedFor != null ? forwardedFor : "unknown";
		TokenDTO token = TokenDTO.builder().connectIp(ipAddress).connectAgent(userAgent).refreshToken(refreshToken).beforeToken(beforeToken).build(); 
		authService.updateToken(token);
		
		String userId = authService.getUserIdByToken(token);
		
		result.put("userId", userId);
		result.put("message", "TOKEN_UPDATE_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	
}
