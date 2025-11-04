package me._hanho.nextjs_shop.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import me._hanho.nextjs_shop.model.Token;
import me._hanho.nextjs_shop.model.User;

@Service
public class AuthService {
	
	private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

	private final PasswordEncoder passwordEncoder;
	
	@Autowired
	private AuthMapper authMapper;
	
	 // 생성자 주입
    public AuthService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
	}
    // 패스워드 확인
    public boolean passwordCheck(String password, String password2) {
		return passwordEncoder.matches(password, password2);
	}
	
	public User getUser(String userId) {
		return authMapper.getUser(userId);
	}
	
	public User getUserExceptPassword(String userId) {
		return authMapper.getUserExceptPassword(userId);
	}
	
	public User getUserByToken(Token token) {
		return authMapper.getUserByToken(token);
	}

	public void insertToken(Token token) {
		authMapper.insertToken(token);
	}
	
	public void joinUser(User user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		authMapper.joinUser(user);
	}

	
	public void userInfoUpdate(User user) {
		int updated = authMapper.userInfoUpdate(user);
	    if (updated == 0) {
	        throw new UserNotFoundException("User not found: " + user.getUserId());
	    }
	}

	public void updateToken(Token token) {
		int tokenId = authMapper.getTokenId(token);
		token.setTokenId(tokenId);
		int updated = authMapper.updateToken(token);
		if (updated == 0) {
	        throw new UserNotFoundException("Token not found: " + token.getTokenId());
	    }
	}

	public boolean getId(String userId) {
		return authMapper.getId(userId) == 1;
	}
	
	
	
}
