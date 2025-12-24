package me._hanho.nextjs_shop.auth;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import me._hanho.nextjs_shop.model.User;

@Service
public class TokenService {
private static final String SECRET_KEY = "HANHOSEONGTOKENTESTHANHOSEONGTOKENTEST";
private static final String SECRET_PHONEAUTH_KEY = "HANHOSEONGPHONEAUTHHANHOSEONGPHONEAUTH";
private static final String SECRET_PHONEAUTH_COMPLETE_KEY = "HANHOSEONGPHONEAUTHCOMPLETEHANHOSEONGPHONEAUTHCOMPLETE";
private static final String SECRET_PWDCHANGE_KEY = "HANHOSEONGPWDCHANGEHANHOSEONGPWDCHANGE";
	
	/**
	 * 토큰 생성하기
	 * @return
	 */
	public String makeJwtToken(int seconds) {
		Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
		
		Date now = new Date();
		Date expire = new Date();
		Long expiredTime = 1000 * 1L; // 현재 1초
		expiredTime = expiredTime * seconds; // 1분 * 원하는 만료 시간(분)
		expire.setTime(expire.getTime() + expiredTime);
		
		return Jwts.builder()
				.setHeaderParam(Header.TYPE, Header.JWT_TYPE)
				.setIssuer("myteam")
				.setIssuedAt(now)
				.setExpiration(expire)
				.signWith(key)
				.compact();
	}
	
	public String makeJwtToken(int seconds, User user) {
		Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
		
		Date now = new Date();
		Date expire = new Date();
		Long expiredTime = 1000 * 1L; // 현재 1분
		expiredTime = expiredTime * seconds; // 1분 * 원하는 만료 시간(분)
		expire.setTime(expire.getTime() + expiredTime);
		
		return Jwts.builder()
				.setHeaderParam(Header.TYPE, Header.JWT_TYPE)
				.setIssuer("myteam")
				.setIssuedAt(now)
				.setExpiration(expire)
				.claim("userId", user.getUserId())
//				.claim("nickName", user.getNickName())
//				.claim("createDate", user.getCreateDate())
				.signWith(key)
				.compact();
	}
	
	/**
	 * 토큰 복호화 하여 본문(Payload) 가져오기
	 * @param token
	 * @return
	 */
	public Claims parseJwtToken(String token) {
		Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
		
		Claims claims = Jwts.parserBuilder()
				.setSigningKey(key)
				.build()
				.parseClaimsJws(token)
				.getBody();
		System.out.println("claims = " + claims.toString());
		
		return claims;
	}
	
	/**
	 * 휴대폰인증 토큰 복호화 하여 본문(Payload) 가져오기
	 * @param token
	 * @return
	 */
	public Claims parseJwtPhoneAuthToken(String token) {
		Key key = Keys.hmacShaKeyFor(SECRET_PHONEAUTH_KEY.getBytes(StandardCharsets.UTF_8));
		
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
		Key key = Keys.hmacShaKeyFor(SECRET_PHONEAUTH_COMPLETE_KEY.getBytes(StandardCharsets.UTF_8));
		
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
		Key key = Keys.hmacShaKeyFor(SECRET_PWDCHANGE_KEY.getBytes(StandardCharsets.UTF_8));
		
		Claims claims = Jwts.parserBuilder()
				.setSigningKey(key)
				.build()
				.parseClaimsJws(token)
				.getBody();
		System.out.println("claims = " + claims.toString());
		
		return claims;
	}
}
