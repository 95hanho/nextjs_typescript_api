package me._hanho.nextjs_shop.auth;

import java.nio.charset.StandardCharsets;
import java.security.Key;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

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
		Key key = Keys.hmacShaKeyFor(REFRESH_SECRET_KEY.getBytes(StandardCharsets.UTF_8));
	    Jwts.parserBuilder()
	            .setSigningKey(key)
	            .build()
	            .parseClaimsJws(token)
	            .getBody();
	}
	
	/**
	 * 토큰 복호화 하여 본문(Payload) 가져오기
	 * @param token
	 * @return
	 */
	public Claims parseJwtToken(String token) {
	    try {
	    	// USER 토큰으로 먼저 시도
	        return parseWithUserKey(token);
	    } catch (JwtException e1) {
	        try {
	        	// 실패하면 SELLER 토큰으로 시도
	            return parseWithSellerKey(token);
	        } catch (JwtException e2) {
	        	// 실패하면 ADMIN 토큰으로 시도
	            return parseWithAdminKey(token);
	         // 이것도 실패하면 JwtInterceptor에서 catch
	        }
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
		Key key = Keys.hmacShaKeyFor(PHONEAUTH_SECRET_KEY.getBytes(StandardCharsets.UTF_8));
		
		Claims claims = Jwts.parserBuilder()
				.setSigningKey(key)
				.build()
				.parseClaimsJws(token)
				.getBody();
		System.out.println("claims = " + claims.toString());
		
		return claims;
	}
	
	/**
	 * 휴대폰인증성공 토큰 복호화 하여 본문(Payload) 가져오기
	 * @param token
	 * @return
	 */
	public Claims parseJwtPhoneAuthCompleteToken(String token) {
		Key key = Keys.hmacShaKeyFor(PHONEAUTH_COMPLETE_SECRET_KEY.getBytes(StandardCharsets.UTF_8));
		
		Claims claims = Jwts.parserBuilder()
				.setSigningKey(key)
				.build()
				.parseClaimsJws(token)
				.getBody();
		System.out.println("claims = " + claims.toString());
		
		return claims;
	}
	
	
	/**
	 * 비밀번호변경 토큰 복호화 하여 본문(Payload) 가져오기
	 * @param token
	 * @return
	 */
	public Claims parseJwtPwdChangeToken(String token) {
		Key key = Keys.hmacShaKeyFor(PWDCHANGE_SECRET_KEY.getBytes(StandardCharsets.UTF_8));
		
		Claims claims = Jwts.parserBuilder()
				.setSigningKey(key)
				.build()
				.parseClaimsJws(token)
				.getBody();
		System.out.println("claims = " + claims.toString());
		
		return claims;
	}
}
