package me._hanho.nextjs_shop.auth;

import java.nio.charset.StandardCharsets;
import java.security.Key;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import me._hanho.nextjs_shop.common.exception.BusinessException;
import me._hanho.nextjs_shop.common.exception.ErrorCode;

@Service
public class TokenService {
	
	private static final String REFRESH_SECRET_KEY = "HANHOSEONGREFRESHTESTHANHOSEONGREFRESHTEST";
	private static final String USER_SECRET_KEY = "HANHOSEONGTOKENTESTHANHOSEONGTOKENTEST";
	private static final String PHONEAUTH_SECRET_KEY = "HANHOSEONGPHONEAUTHHANHOSEONGPHONEAUTH";
	private static final String PHONEAUTH_COMPLETE_SECRET_KEY = "HANHOSEONGPHONEAUTHCOMPLETEHANHOSEONGPHONEAUTHCOMPLETE";
	private static final String PWDCHANGE_SECRET_KEY = "HANHOSEONGPWDCHANGEHANHOSEONGPWDCHANGE";
	private static final String SELLER_SECRET_KEY = "NEXTJSSELLERTOKENTESTNEXTJSSELLERTOKEN";
	private static final String ADMIN_SECRET_KEY = "NEXTJSADMINTOKENTESTNEXTJSADMINTOKENAA";

	/**
	 * 토큰 생성하기
	 * @return
	 */
//	public String makeJwtToken(int seconds) {
//		Key key = Keys.hmacShaKeyFor(USER_SECRET_KEY.getBytes(StandardCharsets.UTF_8));
//		
//		Date now = new Date();
//		Date expire = new Date();
//		Long expiredTime = 1000 * 1L; // 현재 1초
//		expiredTime = expiredTime * seconds; // 1분 * 원하는 만료 시간(분)
//		expire.setTime(expire.getTime() + expiredTime);
//		
//		return Jwts.builder()
//				.setHeaderParam(Header.TYPE, Header.JWT_TYPE)
//				.setIssuer("myteam")
//				.setIssuedAt(now)
//				.setExpiration(expire)
//				.signWith(key)
//				.compact();
//	}
	
//	public String makeJwtToken(int seconds, User user) {
//		Key key = Keys.hmacShaKeyFor(USER_SECRET_KEY.getBytes(StandardCharsets.UTF_8));
//		
//		Date now = new Date();
//		Date expire = new Date();
//		Long expiredTime = 1000 * 1L; // 현재 1분
//		expiredTime = expiredTime * seconds; // 1분 * 원하는 만료 시간(분)
//		expire.setTime(expire.getTime() + expiredTime);
//		
//		return Jwts.builder()
//				.setHeaderParam(Header.TYPE, Header.JWT_TYPE)
//				.setIssuer("myteam")
//				.setIssuedAt(now)
//				.setExpiration(expire)
//				.claim("user", user.getUserId())
//				.signWith(key)
//				.compact();
//	}
	
	/**
	 * 리프레시 토큰 전용
	 * 복호화 하여 본문(Payload) 가져오기
	 * @param token
	 * @return
	 */
	public void parseJwtRefreshToken(String token) {
	    try {
	        Key key = Keys.hmacShaKeyFor(REFRESH_SECRET_KEY.getBytes(StandardCharsets.UTF_8));
	        Jwts.parserBuilder()
	                .setSigningKey(key)
	                .build()
	                .parseClaimsJws(token)
	                .getBody();
	    } catch (JwtException | IllegalArgumentException e) {
	        // IllegalArgumentException: token null/empty 같은 케이스
	        throw new BusinessException(ErrorCode.UNAUTHORIZED_TOKEN, ErrorCode.UNAUTHORIZED_TOKEN.getDefaultMessage(), e);
	    }
	}

	
	/**
	 * 토큰 복호화 하여 본문(Payload) 가져오기
	 * @param token
	 * @return
	 */
	public Claims parseJwtToken(String token) {
	    try {
	        return parseWithUserKey(token);
	    } catch (JwtException e1) {
	        try {
	            return parseWithSellerKey(token);
	        } catch (JwtException e2) {
	            try {
	                return parseWithAdminKey(token);
	            } catch (JwtException e3) {
	                throw new BusinessException(ErrorCode.UNAUTHORIZED_TOKEN, ErrorCode.UNAUTHORIZED_TOKEN.getDefaultMessage(), e3);
	            }
	        }
	    } catch (IllegalArgumentException e) {
	        throw new BusinessException(ErrorCode.UNAUTHORIZED_TOKEN, ErrorCode.UNAUTHORIZED_TOKEN.getDefaultMessage(), e);
	    }
	}
	
	private Claims parseWithUserKey(String token) {
		Key key = Keys.hmacShaKeyFor(USER_SECRET_KEY.getBytes(StandardCharsets.UTF_8));
	    return Jwts.parserBuilder()
	            .setSigningKey(key)
	            .build()
	            .parseClaimsJws(token)
	            .getBody();
	}

	private Claims parseWithSellerKey(String token) {
		Key key = Keys.hmacShaKeyFor(SELLER_SECRET_KEY.getBytes(StandardCharsets.UTF_8));
	    return Jwts.parserBuilder()
	            .setSigningKey(key)
	            .build()
	            .parseClaimsJws(token)
	            .getBody();
	}
	
	private Claims parseWithAdminKey(String token) {
		Key key = Keys.hmacShaKeyFor(ADMIN_SECRET_KEY.getBytes(StandardCharsets.UTF_8));
	    return Jwts.parserBuilder()
	            .setSigningKey(key)
	            .build()
	            .parseClaimsJws(token)
	            .getBody();
	}
	
	/**
	 * 휴대폰인증 토큰 복호화 하여 본문(Payload) 가져오기
	 * @param token
	 * @return
	 */
	public Claims parseJwtPhoneAuthToken(String token) {
	    try {
	        Key key = Keys.hmacShaKeyFor(PHONEAUTH_SECRET_KEY.getBytes(StandardCharsets.UTF_8));
	        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
	    } catch (JwtException | IllegalArgumentException e) {
	        throw new BusinessException(ErrorCode.UNAUTHORIZED_TOKEN, ErrorCode.UNAUTHORIZED_TOKEN.getDefaultMessage(), e);
	    }
	}
	
	/**
	 * 휴대폰인증성공 토큰 복호화 하여 본문(Payload) 가져오기
	 * @param token
	 * @return
	 */
	public Claims parseJwtPhoneAuthCompleteToken(String token) {
		try {
			Key key = Keys.hmacShaKeyFor(PHONEAUTH_COMPLETE_SECRET_KEY.getBytes(StandardCharsets.UTF_8));
			return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
		} catch (JwtException | IllegalArgumentException e) {
	        throw new BusinessException(ErrorCode.UNAUTHORIZED_TOKEN, ErrorCode.UNAUTHORIZED_TOKEN.getDefaultMessage(), e);
	    }
	}
	
	
	/**
	 * 비밀번호변경 토큰 복호화 하여 본문(Payload) 가져오기
	 * @param token
	 * @return
	 */
	public Claims parseJwtPwdChangeToken(String token) {
		try {
			Key key = Keys.hmacShaKeyFor(PWDCHANGE_SECRET_KEY.getBytes(StandardCharsets.UTF_8));
			return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
		} catch (JwtException | IllegalArgumentException e) {
	        throw new BusinessException(ErrorCode.UNAUTHORIZED_TOKEN, ErrorCode.UNAUTHORIZED_TOKEN.getDefaultMessage(), e);
	    }
	}
}
