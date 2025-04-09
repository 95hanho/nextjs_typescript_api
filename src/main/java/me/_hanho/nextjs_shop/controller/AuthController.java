package me._hanho.nextjs_shop.controller;

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
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import me._hanho.nextjs_shop.model.Token;
import me._hanho.nextjs_shop.model.User;
import me._hanho.nextjs_shop.service.AuthService;
import me._hanho.nextjs_shop.service.TokenService;

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
	public ResponseEntity<Map<String, Object>> getUserInfo(@RequestAttribute("id") String id) {
		logger.info("getUserInfo");
		Map<String, Object> result = new HashMap<String, Object>();
		
		if(id != null) {
			User user = authService.getUser(id);
			result.put("msg", "success");
			result.put("user", user);
			return new ResponseEntity<>(result, HttpStatus.OK);
		} else {
			result.put("msg", "token제대로 안됨");
			logger.error("token 제대로 안온듯");
			return new ResponseEntity<>(result, HttpStatus.UNAUTHORIZED);
		}
	}
	
	@PostMapping
	public ResponseEntity<Map<String, Object>> login(@ModelAttribute User user, @RequestHeader("user-agent") String agent
			, HttpServletRequest request) {
		logger.info("login :" + user.getId());
		Map<String, Object> result = new HashMap<String, Object>();

		User checkUser = authService.getUser(user);
		if (checkUser == null || !authService.passwordCheck(user.getPassword(), checkUser.getPassword())) {
			result.put("msg", "입력하신 아이디 또는 비밀번호가 일치하지 않습니다");
			result.put("response_code", 430);
			result.put("status", "success");
			logger.error("입력하신 아이디 또는 비밀번호가 일치하지 않습니다");
			
			return new ResponseEntity<>(
					result
					, HttpStatus.BAD_REQUEST);
		} else {
			User onlyId = new User();
			onlyId.setId(checkUser.getId());
			String accessToken = tokenService.makeJwtToken(600, onlyId);
			String refreshToken = tokenService.makeJwtToken(1800);
			String ipAddress = request.getRemoteAddr();
			Token token = new Token(ipAddress, agent, refreshToken, checkUser.getId());
			authService.insertToken(token);
			
			result.put("msg", "로그인");
			result.put("status", "success");
			result.put("response_code", 200);
			result.put("access_token", accessToken);
			result.put("refresh_token", refreshToken);
			return new ResponseEntity<>(
					result
					, HttpStatus.OK);
		}
	}
	
	
	@GetMapping("/id")
	public ResponseEntity<Map<String, Object>> idDuplcheck(@RequestParam("id") String id) {
		logger.info("idDuplcheck");
		Map<String, Object> result = new HashMap<String, Object>();
		
		boolean hasId = authService.getId(id);
		
		if(!hasId) {
			result.put("msg", "success");
			return new ResponseEntity<>(result, HttpStatus.OK);
		} else {
			result.put("msg", "fail");
			return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
		}
	}
	
	@PostMapping("/phone")
	public ResponseEntity<Map<String, Object>> phoneAuth(@RequestParam("phone") String phone) {
		logger.info("phoneAuth");
		Map<String, Object> result = new HashMap<String, Object>();
		
		result.put("msg", "success");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	@PostMapping("/member")
	public ResponseEntity<Map<String, Object>> join(@ModelAttribute User user) {
		logger.info("join : " + user);
		Map<String, Object> result = new HashMap<String, Object>();
		
		authService.joinMember(user);
		
		result.put("msg", "success");
		return new ResponseEntity<>(null, HttpStatus.OK);
	}
	
	@PostMapping("/token")
	public ResponseEntity<Map<String, Object>> reToken(@RequestParam("refresh_token") String refresh_token,
			HttpServletRequest request, @RequestHeader("user-agent") String agent) {
		logger.info("reToken " + refresh_token);
		Map<String, Object> result = new HashMap<String, Object>();
		
		Claims claims = null;
		try {
			claims = tokenService.parseJwtToken(refresh_token);
		} catch (Exception e) {
			result.put("msg", "token제대로 안됨");
			return new ResponseEntity<>(result, HttpStatus.UNAUTHORIZED);
		}
		
		if(claims != null) {
			String ipAddress = request.getRemoteAddr();
			Token token = new Token(ipAddress, agent, refresh_token);
			User checkUser = authService.getUser(token);
			
			if(checkUser != null) {
				User onlyId = new User();
				onlyId.setId(checkUser.getId());
				String accessToken = tokenService.makeJwtToken(600, onlyId);
				String refreshToken = tokenService.makeJwtToken(1800);
				Token token2 = new Token(ipAddress, agent, refreshToken, checkUser.getId());
				authService.updateToken(token2);
				
				result.put("msg", "access 토큰 재발급 성공");
				result.put("access_token", accessToken);
				result.put("refresh_token", refreshToken);
				result.put("response_code", 200);
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
