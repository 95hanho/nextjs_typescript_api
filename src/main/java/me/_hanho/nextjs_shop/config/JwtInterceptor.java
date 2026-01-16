package me._hanho.nextjs_shop.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import me._hanho.nextjs_shop.auth.TokenService;

@Component
@RequiredArgsConstructor
public class JwtInterceptor implements HandlerInterceptor {

	private static final Logger logger = LoggerFactory.getLogger(JwtInterceptor.class);
	
	private final TokenService tokenService;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String authorizationHeader = request.getHeader("Authorization");

//		logger.info(authorizationHeader);
		String token = null;
		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			token = authorizationHeader.substring(7); // "Bearer " 이후의 문자열만 추출
	    }
		
		/* =======판매자 로그인 토큰==================================================================== */
		logger.info("preHandle ===> url : " + request.getRequestURL());
		if (token != null && !token.isEmpty()) {
			logger.info("token : " + token);
            try {
                // JWT 파싱 및 복호화
                Claims claims = tokenService.parseJwtToken(token);
                
                /* 파싱이 되는지만 확인할거임 */
                // 타입 확인(유저/판매자)
                String type = claims.get("type", String.class);
                logger.info("type : " + type);
                if (type == null) {
                    throw new IllegalArgumentException("Token type is missing");
                } 
                // 유저 a토큰
                else if("ACCESS".equals(type)) {
                	// userNo 추출
                	String userNo = claims.get("userNo", String.class);
                	if (userNo == null) {
                        throw new SecurityException("Invalid ACCESS token: missing userNo");
                    }
                	logger.info("type : " + type + ", userNo : " + userNo);
                	// HttpServletRequest에 userNo 추가
                	request.setAttribute("userNo", userNo);
                } 
                // 판매자 토큰
                else if("SELLER".equals(type)) {
                	// sellerNo 추출
                    String sellerNo = claims.get("sellerNo", String.class);
                    if (sellerNo == null) {
                        throw new SecurityException("Invalid SELLER token: missing sellerNo");
                    }
                    logger.info("type : " + type + ", sellerNo : " + sellerNo);
                    // HttpServletRequest에 sellerNo 추가
                    request.setAttribute("sellerNo", sellerNo);
                }
                // 관리자 토큰
                else if("ADMIN".equals(type)) {
                	// adminNo 추출
                    String adminNo = claims.get("adminNo", String.class);
                    if (adminNo == null) {
                        throw new SecurityException("Invalid ADMIN token: missing adminNo");
                    }
                    logger.info("type : " + type + ", adminNo : " + adminNo);
                    // HttpServletRequest에 sellerNo 추가
                    request.setAttribute("adminNo", adminNo);
                }
            } catch (Exception e) {
                // 토큰이 유효하지 않으면 요청을 거부
            	logger.error("token UNAUTHORIZED");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return false;
            }
        }
		/* =======휴대폰 인증 토큰==================================================================== */
		String phoneAuthToken = request.getHeader("X-Phone-Auth-Token");
		logger.info(phoneAuthToken);
		if (phoneAuthToken != null && !phoneAuthToken.isEmpty()) {
			logger.info("phoneAuthToken : " + phoneAuthToken);
            try {
                // JWT 파싱 및 복호화
                Claims claims = tokenService.parseJwtPhoneAuthToken(phoneAuthToken);

                /* 파싱이 되는지만 확인할거임 */
                // userNo 추출
                String phoneUserId = claims.get("phoneUserId", String.class);
                logger.info("phoneUserId : " + phoneUserId);
                
                // HttpServletRequest에 userNo 추가
                request.setAttribute("phoneUserId", phoneUserId);
                
            } catch (Exception e) {
                // 토큰이 유효하지 않으면 요청을 거부
            	logger.error("phoneAuthToken UNAUTHORIZED");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return false;
            }
        }
		/* ===================================================================================== */
        return true;
	}
}
