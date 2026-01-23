package me._hanho.nextjs_shop.auth;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me._hanho.nextjs_shop.common.exception.BusinessException;
import me._hanho.nextjs_shop.common.exception.ErrorCode;
import me._hanho.nextjs_shop.model.PhoneAuth;

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
	public ResponseEntity<Map<String, Object>> getUserInfo(@RequestAttribute(value="userNo", required=false) Integer userNo) {
		if (userNo == null) throw new BusinessException(ErrorCode.AUTHENTICATION_REQUIRED);
		logger.info("getUserInfo : userNo=" + userNo);
		Map<String, Object> result = new HashMap<String, Object>();
		
		UserInfo user = authService.getUserInfo(userNo);
		if(user == null) {
			throw new BusinessException(ErrorCode.USER_NOT_FOUND);
		}
		result.put("user", user);
		result.put("message", "USER_FETCH_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
		
	}
	// 로그인
	@PostMapping
	public ResponseEntity<Map<String, Object>> login(@RequestParam("userId") String userId, @RequestParam("password") String password, 
			@RequestHeader("User-Agent") String agent
			, HttpServletRequest request) {
		logger.info("login :" + userId);
		Map<String, Object> result = new HashMap<String, Object>();
		
		UserLoginResponse checkUser = authService.getUserForPassword(userId);
		if (checkUser == null || !authService.passwordCheck(password, checkUser.getPassword())) {
			throw new BusinessException(ErrorCode.LOGIN_FAILED);
		}
		result.put("userNo", checkUser.getUserNo());
		result.put("message", "LOGIN_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	// 유저아이디 조회 By인증토큰
	@GetMapping("/id")
	public ResponseEntity<Map<String, Object>> getUserId(@RequestAttribute(value="userNo", required=false) Integer userNo) {
		if (userNo == null) throw new BusinessException(ErrorCode.AUTHENTICATION_REQUIRED);
		logger.info("getUserId userNo : " + userNo);
		Map<String, Object> result = new HashMap<String, Object>();
		
		String userId = authService.getUserId(userNo);
		
		result.put("userId", userId);
		result.put("message", "ID_AVAILABLE");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	// 아이디 중복확인
	@PostMapping("/id")
	public ResponseEntity<Map<String, Object>> idDuplcheck(@RequestParam("userId") String userId) {
		logger.info("idDuplcheck userId : " + userId);
		Map<String, Object> result = new HashMap<String, Object>();
		
		boolean hasId = authService.hasId(userId);
		logger.info("hasId : " + hasId);
		
		if(hasId) throw new BusinessException(ErrorCode.ID_DUPLICATED);
		
		result.put("message", "ID_AVAILABLE");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	// 휴대폰인증 - 회원가입, 아이디찾기, 비밀번호 찾기 또는 휴대폰번호 바꾸기
	@PostMapping("/phone")
	public ResponseEntity<Map<String, Object>> sendPhoneAuth(
			@RequestParam("phoneAuthToken") String phoneAuthToken,
			@RequestHeader("user-agent") String userAgent, 
			@RequestHeader("x-forwarded-for") String forwardedFor,
			@RequestAttribute(required = false, name = "userNo") Integer userNo) {
		logger.info("phoneAuth - phoneAuthToken : {}, userNo : {}", phoneAuthToken, userNo);
		Map<String, Object> result = new HashMap<String, Object>();
		
		String ipAddress = forwardedFor != null ? forwardedFor : "unknown";
		String phone = null;
		
		try {
			// JWT 파싱 및 복호화
			Claims claims = tokenService.parseJwtPhoneAuthToken(phoneAuthToken);
			String type = claims.get("type", String.class);
            logger.info("type : " + type);
            if (type == null) {
                throw new BusinessException(ErrorCode.PHONE_AUTH_TOKEN_INVALID);
            }

            if (!"PHONEAUTH".equals(type)) {
                throw new BusinessException(ErrorCode.PHONE_AUTH_TOKEN_WRONG_TYPE);
            }
        	phone = claims.get("phone", String.class);
		} catch (ExpiredJwtException e) {
			throw new BusinessException(ErrorCode.PHONE_AUTH_TOKEN_EXPIRED);
	    } catch (JwtException | IllegalArgumentException e) {
	        throw new BusinessException(ErrorCode.PHONE_AUTH_TOKEN_INVALID);
	    }
		if (phone == null || phone.isEmpty()) {
	        throw new BusinessException(ErrorCode.PHONE_NOT_FOUND_IN_TOKEN);
	    }
		
		// 인증번호(6자리?)를 생성
		String verificationCode = phoneAuthCodeService.generate6DigitCode();
		logger.info("phoneAuth-verificationCode : " + verificationCode);
		
        // 토큰을 휴대폰인증DB에 (id, userId, phoneAuthToken, phone, verificationCode) 형태로 저장
		PhoneAuthDAO phoneAuth = PhoneAuthDAO.builder().userNo(userNo).phoneAuthToken(phoneAuthToken)
				.phone(phone).verificationCode(verificationCode).connectIp(ipAddress).connectAgent(userAgent).build();
		authService.insertPhoneAuth(phoneAuth);
		
		// 인증번호를 휴대폰으로 보냄!!
		//
		
		result.put("message", "VERIFICATION_SENT");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	// 휴대폰인증 확인!!
	@PostMapping("/phone/check")
	public ResponseEntity<Map<String, Object>> phoneAuthCheck(
			@RequestParam("authNumber") String authNumber,
			@RequestParam("phoneAuthToken") String phoneAuthToken,
			@RequestParam(required = false, name = "userId") String requestId) {
		logger.info("phoneAuthCheck - authNumber='" + authNumber + ", requestId : " + requestId);
		Map<String, Object> result = new HashMap<String, Object>();
		
		try {
			// JWT 파싱 및 복호화
			Claims claims = tokenService.parseJwtPhoneAuthToken(phoneAuthToken);
			String type = claims.get("type", String.class);
            logger.info("type : " + type);
            if (type == null) {
                throw new BusinessException(ErrorCode.PHONE_AUTH_TOKEN_INVALID);
            }
            if (!"PHONEAUTH".equals(type)) {
                throw new BusinessException(ErrorCode.PHONE_AUTH_TOKEN_WRONG_TYPE);
            }
		} catch (ExpiredJwtException e) {
			throw new BusinessException(ErrorCode.PHONE_AUTH_TOKEN_EXPIRED);
	    } catch (JwtException | IllegalArgumentException e) {
	        throw new BusinessException(ErrorCode.PHONE_AUTH_TOKEN_INVALID);
	    }
		
		PhoneAuth phoneAuth = authService.getPhoneAuthCode(phoneAuthToken);
	    if (phoneAuth == null) {
	        throw new BusinessException(ErrorCode.PHONE_AUTH_NOT_FOUND);
	    }
	    // 인증번호가 올바르지 않음
	    if (!authNumber.equals(phoneAuth.getVerificationCode())) {
	        throw new BusinessException(ErrorCode.INVALID_VERIFICATION_CODE);
	    }
		// 인증번호 검증 성공(사용토큰으로 변경)
		authService.markPhoneAuthUsed(phoneAuth.getPhoneAuthId());
        
		String hasId = authService.getUserIdByPhone(phoneAuth.getPhone());
        if(requestId == null) {
        	// 1. requestId가 없고,hasId에 id도 없다. = 회원가입
        	if(hasId == null) {
        		result.put("message", "PHONEAUTH_VALIDATE"); // 인증 성공
    			return new ResponseEntity<>(result, HttpStatus.OK);
        	}
        	// 2. requestId가 없고, hasId가 존재하는 유저이다.  = 아이디찾기
        	else {
        		result.put("userId", hasId);
        		result.put("message", "IDFIND_SUCCESS"); // 인증 성공
        		return new ResponseEntity<>(result, HttpStatus.OK);
        	}
        }
        // 3. requestId가 있고, hasId와 일치한다. = 비밀번호 찾기
        else if(requestId != null && requestId.equals(hasId)) {
        	result.put("userId", hasId);
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
	public ResponseEntity<Map<String, Object>> join(@Valid @ModelAttribute JoinRequest user) {
		logger.info("join : " + user);
		Map<String, Object> result = new HashMap<String, Object>();
		
		authService.joinUser(user);
		
		result.put("message", "JOIN_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	// 회원 정보 변경
	@PutMapping("/user")
	public ResponseEntity<Map<String, Object>> userInfoUpdate(
			@Valid @ModelAttribute UpdateUserRequest user, 
			@RequestAttribute(value="userNo", required=false) Integer userNo) {
		if (userNo == null) throw new BusinessException(ErrorCode.AUTHENTICATION_REQUIRED);
		logger.info("userInfoUpdate : 회원 정보 변경 - " + user);
		Map<String, Object> result = new HashMap<String, Object>();
		
		authService.userInfoUpdate(user, userNo);
		
		result.put("message", "USER_UPDATE_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	// 비밀번호 변경 - 비밀번호찾기 또는 마이페이지 비번변경
	@PostMapping("/password")
	public ResponseEntity<Map<String, Object>> passwordChange(
			@RequestParam("curPassword") String curPassword,
			@RequestParam("newPassword") String newPassword,
			@RequestParam("pwdResetToken") String pwdResetToken) {
		Map<String, Object> result = new HashMap<String, Object>();
		Integer userNo = null;
		
		try {
			// JWT 파싱 및 복호화
			Claims claims = tokenService.parseJwtPhoneAuthToken(pwdResetToken);
			String type = claims.get("type", String.class);
            logger.info("type : " + type);
            if (type == null) {
                throw new BusinessException(ErrorCode.PWD_RESET_TOKEN_INVALID);
            }
            if (!"PWDRESET".equals(type)) {
                throw new BusinessException(ErrorCode.PWD_RESET_TOKEN_WRONG_TYPE);
            }
            userNo = claims.get("userNo", Integer.class);
		} catch (ExpiredJwtException e) {
			throw new BusinessException(ErrorCode.PWD_RESET_TOKEN_EXPIRED);
	    } catch (JwtException | IllegalArgumentException e) {
	        throw new BusinessException(ErrorCode.PWD_RESET_TOKEN_INVALID);
	    }
		if (userNo == null) {
	        throw new BusinessException(ErrorCode.USER_NO_NOT_FOUND_IN_TOKEN);
	    }
		
		// 비밀번호 확인을 위한 유저 조회
		String checkPassword = authService.getPassword(userNo);
		// 현재 비밀번호가 있다면 마이페이지 비번변경(없으면 비밀번호찾기에서 바꾸는거)
		if(curPassword != null) {
			// 현재 비밀번호가 일치하는 지 검사.
			if(!authService.passwordCheck(curPassword, checkPassword)) {
				throw new BusinessException(ErrorCode.CURRENT_PASSWORD_MISMATCH);
			}
		}
		// 현재비밀번호와 새 비밀번호 같은지 검사
		if(authService.passwordCheck(newPassword, checkPassword)) {
			throw new BusinessException(ErrorCode.CURRENT_PASSWORD_EQUAL);
		}
		
		// 비밀번호 변경
		authService.changePassword(userNo, newPassword);
		
		result.put("message", "PASSWORD_CHANGE_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	// 로그인 토큰 저장
	@PostMapping("/token")
	public ResponseEntity<Map<String, Object>> insertToken(
			@RequestAttribute(value="userNo", required=false) Integer userNo, @RequestParam("refreshToken") String refreshToken,
			@RequestHeader("user-agent") String userAgent, @RequestHeader("x-forwarded-for") String forwardedFor) {
		if (userNo == null) throw new BusinessException(ErrorCode.AUTHENTICATION_REQUIRED);
		logger.info("insertToken refreshToken : " + refreshToken.substring(refreshToken.length() - 10) + ", userNo : " + userNo + 
				", user-agent : " + userAgent + ", x-forwarded-for : " + forwardedFor);
		Map<String, Object> result = new HashMap<String, Object>();
		
		tokenService.parseJwtRefreshToken(refreshToken);
		
		String ipAddress = forwardedFor != null ? forwardedFor : "unknown";
		UserToken token = UserToken.builder().connectIp(ipAddress).connectAgent(userAgent).refreshToken(refreshToken).userNo(userNo).build(); 
		authService.insertToken(token);
		
		result.put("message", "TOKEN_INSERT_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	// 로그인 토큰 수정(재저장)
	@PostMapping("/token/refresh")
	public ResponseEntity<Map<String, Object>> updateToken(
			@RequestParam("beforeToken") String beforeToken , 
			@RequestParam("refreshToken") String refreshToken,
			@RequestHeader("user-agent") String userAgent, 
			@RequestHeader("x-forwarded-for") String forwardedFor) {
		logger.info("updateToken beforeToken : " + beforeToken.substring(beforeToken.length() - 10) + 
				", refreshToken : " + refreshToken.substring(refreshToken.length() - 10) + ", user-agent : " + userAgent + 
				", x-forwarded-for : " + forwardedFor);
		Map<String, Object> result = new HashMap<String, Object>();
		
		tokenService.parseJwtRefreshToken(beforeToken);
		tokenService.parseJwtRefreshToken(refreshToken);
		
		String ipAddress = forwardedFor != null ? forwardedFor : "unknown";
		ReToken token = ReToken.builder().connectIp(ipAddress).connectAgent(userAgent).refreshToken(refreshToken).beforeToken(beforeToken).build(); 
		
		authService.updateToken(token);
		
		Integer userNo = authService.getUserNoByToken(token);
		if(userNo == null) {
			result.put("message", "WRONG_TOKEN");
			return new ResponseEntity<>(result, HttpStatus.UNAUTHORIZED);
		}
		
		result.put("userNo", userNo);
		result.put("message", "TOKEN_UPDATE_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	// 회원탈퇴 요청
	@DeleteMapping
	public ResponseEntity<Map<String, Object>> withDrawalUser(@RequestAttribute(value="userNo", required=false) Integer userNo) {
		if (userNo == null) throw new BusinessException(ErrorCode.AUTHENTICATION_REQUIRED);
		logger.info("withDrawalUser userNo : " + userNo);
		Map<String, Object> result = new HashMap<String, Object>();
		
		authService.withDrawalUser(userNo);
		
		result.put("message", "USER_WITHDRAWAL_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	

	
}
