package me._hanho.nextjs_shop.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import me._hanho.nextjs_shop.auth.TokenService;

@Component
public class JwtInterceptor implements HandlerInterceptor {

	private static final Logger logger = LoggerFactory.getLogger(JwtInterceptor.class);
	
	@Autowired
	private TokenService tokenService;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// 어차피 안쓸듯 = 'Authorization'만 안보내면 상관없음.
		String authorizationHeader = request.getHeader("Authorization");
		logger.info(authorizationHeader);
		String accessToken = null;
		 
		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			accessToken = authorizationHeader.substring(7); // "Bearer " 이후의 문자열만 추출
	    }
//		logger.info("preHandle ===> accessToken : " + accessToken);
		
		if (accessToken != null && !accessToken.isEmpty()) {
			logger.info("accessToken : " + accessToken);
            try {
                // JWT 파싱 및 복호화
                Claims claims = tokenService.parseJwtToken(accessToken);

                // userId 추출
                String userId = claims.get("userId", String.class);

                // HttpServletRequest에 userId 추가
                request.setAttribute("userId", userId);
            } catch (Exception e) {
                // 토큰이 유효하지 않으면 요청을 거부
            	logger.error("token UNAUTHORIZED");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return false;
            }
        }

        return true;
	}
}
