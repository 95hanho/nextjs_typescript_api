package me._hanho.nextjs_shop.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import me._hanho.nextjs_shop.model.PhoneAuth;
import me._hanho.nextjs_shop.model.Token;
import me._hanho.nextjs_shop.model.User;

@Service
@RequiredArgsConstructor
public class AuthService {
	
	private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    private final PasswordEncoder passwordEncoder;
    private final AuthMapper authMapper;
	
	public User getUserExceptPassword(String userId) {
		return authMapper.getUserExceptPassword(userId);
	}
	
	public User getUser(String userId) {
		return authMapper.getUser(userId);
	}
	
    // 패스워드 확인
    public boolean passwordCheck(String password, String checkPassword) {
		return passwordEncoder.matches(password, checkPassword);
	}
	
	public boolean getId(String userId) {
		return authMapper.getId(userId) == 1;
	}
	
	public void insertPhoneAuth(PhoneAuth phoneAuth) {
		authMapper.insertPhoneAuth(phoneAuth);
	}
	
	public PhoneAuth getPhoneAuthCode(String phoneAuthToken) {
		return authMapper.getPhoneAuthCode(phoneAuthToken);
	}
	
	public void markPhoneAuthUsed(int phoneAuthId) {
		authMapper.markPhoneAuthUsed(phoneAuthId);
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
	
	public void changePassword(String userId, String newPassword) {
		authMapper.changePassword(userId, passwordEncoder.encode(newPassword));
	}
	
	public void insertToken(Token token) {
		authMapper.insertToken(token);
	}
	
	public void updateToken(TokenDTO token) {
		int updated = authMapper.updateToken(token);
		if (updated == 0) {
	        throw new UserNotFoundException("Token not found: " + token.getTokenId());
	    }
	}

	public String getUserIdByToken(TokenDTO token) {
		return authMapper.getUserIdByToken(token);
	}








}
