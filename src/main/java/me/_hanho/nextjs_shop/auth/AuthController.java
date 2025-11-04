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

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import me._hanho.nextjs_shop.model.Token;
import me._hanho.nextjs_shop.model.User;

@RestController
@RequestMapping("/bapi/auth")
public class AuthController {
	
	private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

	@Autowired
	private AuthService authService;
	
	@Autowired
	private TokenService tokenService;
	
	// 유저정보가져오기
	@GetMapping
	public ResponseEntity<Map<String, Object>> getUserInfo(@RequestAttribute("userId") String userId) {
		logger.info("getUserInfo : userId=" + userId);
		Map<String, Object> result = new HashMap<String, Object>();
		
		if(userId != null) {
			User user = authService.getUserExceptPassword(userId);
			result.put("user", user);
			result.put("msg", "success");
			return new ResponseEntity<>(result, HttpStatus.OK);
		} else {
			result.put("msg", "token제대로 안됨");
			logger.error("token 제대로 안온듯");
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
			result.put("msg", "USER_NOT_FOUND"); // 입력하신 아이디 또는 비밀번호가 일치하지 않습니다
			result.put("code", 430);
			logger.error("입력하신 아이디 또는 비밀번호가 일치하지 않습니다");
			
			return new ResponseEntity<>(
					result
					, HttpStatus.BAD_REQUEST);
		} else {
			User onlyId = new User();
			onlyId.setUserId(checkUser.getUserId());
			String accessToken = tokenService.makeJwtToken(600, onlyId);
			String refreshToken = tokenService.makeJwtToken(1800);
			String ipAddress = request.getRemoteAddr();
			Token token = Token.builder().connectIp(ipAddress).connectAgent(agent).refreshToken(refreshToken).userId(checkUser.getUserId()).build();
			authService.insertToken(token);
			
			result.put("msg", "success");
			result.put("code", 200);
			result.put("accessToken", accessToken);
			result.put("refreshToken", refreshToken);
			return new ResponseEntity<>(
					result
					, HttpStatus.OK);
		}
	}
	// 아이디 중복확인 
	@GetMapping("/id")
	public ResponseEntity<Map<String, Object>> idDuplcheck(@RequestParam("userId") String userId) {
		logger.info("idDuplcheck");
		Map<String, Object> result = new HashMap<String, Object>();
		
		boolean hasId = authService.getId(userId);
		
		if(!hasId) {
			result.put("msg", "success");
			return new ResponseEntity<>(result, HttpStatus.OK);
		} else {
			result.put("msg", "fail");
			return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
		}
	}
	// 휴대폰인증
	@PostMapping("/phone")
	public ResponseEntity<Map<String, Object>> phoneAuth(@RequestParam("phone") String phone) {
		logger.info("phoneAuth");
		Map<String, Object> result = new HashMap<String, Object>();
		
		result.put("msg", "success");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	// 회원가입
	@PostMapping("/user")
	public ResponseEntity<Map<String, Object>> join(@ModelAttribute User user) {
		logger.info("join : " + user);
		Map<String, Object> result = new HashMap<String, Object>();
		
		authService.joinUser(user);
		
		result.put("msg", "success");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	// 회원 정보 변경
	@PutMapping("/user")
	public ResponseEntity<Map<String, Object>> userInfoUpdate(@ModelAttribute User user) {
		logger.info("userInfoUpdate : 회원 정보 변경 - " + user);
		Map<String, Object> result = new HashMap<String, Object>();
		
		authService.userInfoUpdate(user);
		
		result.put("msg", "success");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	// 토큰 재생성
	@PostMapping("/token")
	public ResponseEntity<Map<String, Object>> reToken(@RequestParam("refreshToken") String refreshToken,
			HttpServletRequest request, @RequestHeader("user-agent") String agent) {
		logger.info("reToken " + refreshToken);
		Map<String, Object> result = new HashMap<String, Object>();
		
		Claims claims = null;
		try {
			claims = tokenService.parseJwtToken(refreshToken);
		} catch (Exception e) {
			result.put("msg", "token제대로 안됨");
			return new ResponseEntity<>(result, HttpStatus.UNAUTHORIZED);
		}
		
		if(claims != null) {
			String ipAddress = request.getRemoteAddr();
			Token token = Token.builder().connectIp(ipAddress).connectAgent(agent).refreshToken(refreshToken).build();
			User checkUser = authService.getUserByToken(token);
			
			if(checkUser != null) {
				User onlyId = new User();
				onlyId.setUserId(checkUser.getUserId());
				String updatedAccessToken = tokenService.makeJwtToken(600, onlyId);
				String updatedRefreshToken = tokenService.makeJwtToken(1800);
				Token token2 = Token.builder().connectIp(ipAddress).connectAgent(agent).refreshToken(updatedRefreshToken).userId(checkUser.getUserId()).build();
				authService.updateToken(token2);
				
				result.put("msg", "access 토큰 재발급 성공");
				result.put("accessToken", updatedAccessToken);
				result.put("refreshToken", updatedRefreshToken);
				result.put("code", 200);
				result.put("status", "success");
				return new ResponseEntity<>(result, HttpStatus.OK);
			} else {
				result.put("msg", "token제대로 안됨");
				return new ResponseEntity<>(result, HttpStatus.UNAUTHORIZED);
			}
		} else {
			result.put("msg", "token제대로 안됨");
			return new ResponseEntity<>(result, HttpStatus.UNAUTHORIZED);
		}
	}
	

	
}
