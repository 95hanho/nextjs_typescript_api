package me._hanho.nextjs_shop.config;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import me._hanho.nextjs_shop.auth.TokenService;
import me._hanho.nextjs_shop.common.exception.BusinessException;
import me._hanho.nextjs_shop.common.exception.ErrorCode;

@Component
@RequiredArgsConstructor
public class JwtInterceptor implements HandlerInterceptor {

	private static final Logger logger = LoggerFactory.getLogger(JwtInterceptor.class);
	
	private final TokenService tokenService;
	
	private void writeJson(HttpServletResponse response, int status, String messageCode) throws IOException {
	    response.setStatus(status);
	    response.setContentType("application/json;charset=UTF-8");
	    response.getWriter().write("{\"message\":\"" + messageCode + "\"}");
	}

	private void unauthorized(HttpServletResponse response, String messageCode) throws IOException {
	    writeJson(response, HttpServletResponse.SC_UNAUTHORIZED, messageCode);
	}
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		try {
			String authorizationHeader = request.getHeader("Authorization");
	//		logger.info(authorizationHeader);
			String token = null;
			if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
				token = authorizationHeader.substring(7); // "Bearer " 이후의 문자열만 추출
		    }
			
			/* =======판매자 로그인 토큰==================================================================== */
			logger.info("token : " + token);
			logger.info("preHandle ===> method: " + request.getMethod() + ", url : " + request.getRequestURL());
			if (token != null && !token.isEmpty()) {
				// JWT 파싱 및 복호화
	            Claims claims = tokenService.parseJwtToken(token);
	                
	            if(token != null && !token.isEmpty()) {
	                /* 파싱이 되는지만 확인할거임 */
	                // 타입 확인(유저/판매자)
	                String type = claims.get("type", String.class);
	                logger.info("type : " + type);
	                if (type == null) {
	                	 throw new BusinessException(ErrorCode.UNAUTHORIZED_TOKEN, "Token type is missing");
	                }
	                // 유저 a토큰
	                if("ACCESS".equals(type)) {
	                	// userNo 추출
	                	Integer userNo = claims.get("userNo", Integer.class);
	                	if (userNo == null) throw new BusinessException(ErrorCode.UNAUTHORIZED_TOKEN, "Invalid ACCESS token: missing userNo");
	                	logger.info("type : " + type + ", userNo : " + userNo);
	                	request.setAttribute("userNo", userNo);
	                }
	                // 판매자 토큰
	                else if("SELLER".equals(type)) {
	                	// sellerNo 추출
	                	Integer sellerNo = claims.get("sellerNo", Integer.class);
	                	if (sellerNo == null) throw new BusinessException(ErrorCode.UNAUTHORIZED_TOKEN, "Invalid SELLER token: missing sellerNo");
	                    logger.info("type : " + type + ", sellerNo : " + sellerNo);
	                    request.setAttribute("sellerNo", sellerNo);
	                }
	                // 관리자 토큰
	                else if("ADMIN".equals(type)) {
	                	// adminNo 추출
	                	Integer adminNo = claims.get("adminNo", Integer.class);
	                	if (adminNo == null) throw new BusinessException(ErrorCode.UNAUTHORIZED_TOKEN, "Invalid ADMIN token: missing adminNo");
	                    logger.info("type : " + type + ", adminNo : " + adminNo);
	                    request.setAttribute("adminNo", adminNo);
	                } else {
	                	throw new BusinessException(ErrorCode.UNAUTHORIZED_TOKEN, "Unknown token type");
	                }
	            }
	        }
			/* =======휴대폰 인증 토큰==================================================================== */
			String phoneAuthToken = request.getHeader("X-Phone-Auth-Token");
			logger.info("phoneAuthToken : " + phoneAuthToken);
			if (phoneAuthToken != null && !phoneAuthToken.isEmpty()) {
	            // JWT 파싱 및 복호화
	            Claims claims = tokenService.parseJwtPhoneAuthToken(phoneAuthToken);
	
	            /* 파싱이 되는지만 확인할거임 */
	            String phoneUserId = claims.get("phoneUserId", String.class);
	            if (phoneUserId == null || phoneUserId.isEmpty()) {
	                throw new BusinessException(ErrorCode.UNAUTHORIZED_TOKEN, "Invalid phoneAuth token: missing phoneUserId");
	            }
	            request.setAttribute("phoneUserId", phoneUserId);
	        }
			/* ===================================================================================== */
	        return true;
		} catch (BusinessException e) {
			logger.error("UNAUTHORIZED: " + e.getErrorCode().getCode(), e);
			unauthorized(response, e.getErrorCode().getCode()); // ✅ {message:"UNAUTHORIZED_TOKEN"} 형태로 내려감
			return false;
		} catch (Exception e) {
			logger.error("SERVER_ERROR in JwtInterceptor", e);
	        writeJson(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SERVER_ERROR");
	        return false;
		}
	}
}
