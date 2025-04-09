package me._hanho.nextjs_shop.config;

import java.util.Enumeration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import me._hanho.nextjs_shop.service.FileService;
import me._hanho.nextjs_shop.service.TokenService;

@Component
public class JwtInterceptor implements HandlerInterceptor {

	private static final Logger logger = LoggerFactory.getLogger(JwtInterceptor.class);
	
	@Autowired
	private TokenService tokenService;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String access_token = request.getHeader("authorization");
//		logger.info("preHandle ===> access_token : " + access_token);
		
		if (access_token != null && !access_token.isEmpty()) {
			logger.info("access_token : " + access_token);
            try {
                // JWT 파싱 및 복호화
                Claims claims = tokenService.parseJwtToken(access_token);

                // login_id 추출
                String id = claims.get("id", String.class);

                // HttpServletRequest에 login_id 추가
                request.setAttribute("id", id);
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
