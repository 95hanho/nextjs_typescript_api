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
    
    public boolean passwordCheck(String password, String password2) {
		return passwordEncoder.matches(password, password2);
	}
	
	public User getUser(String user_id) {
		return authMapper.getUser(user_id);
	}
	
	public User getUserExceptPassword(String user_id) {
		return authMapper.getUserExceptPassword(user_id);
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
	        throw new UserNotFoundException("User not found: " + user.getUser_id());
	    }
	}

	public void updateToken(Token token) {
		int token_id = authMapper.getToken_id(token);
		token.setToken_id(token_id);
		int updated = authMapper.updateToken(token);
		if (updated == 0) {
	        throw new UserNotFoundException("Token not found: " + token.getToken_id());
	    }
	}

	public boolean getId(String user_id) {
		return authMapper.getId(user_id) == 1;
	}
	
	
	
}
