package me._hanho.nextjs_shop.auth;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import me._hanho.nextjs_shop.common.exception.BusinessException;
import me._hanho.nextjs_shop.common.exception.ErrorCode;
import me._hanho.nextjs_shop.model.PhoneAuth;

@Service
@RequiredArgsConstructor
public class AuthService {
	
//	private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    private final PasswordEncoder passwordEncoder;
    private final AuthMapper authMapper;
	
	public UserInfo getUserInfo(Integer userNo) {
		return authMapper.getUserInfo(userNo);
	}
	
	public UserLoginResponse getUserForPassword(String userId) {
		return authMapper.getUserForPassword(userId);
	}
	
    // 패스워드 확인
    public boolean passwordCheck(String password, String checkPassword) {
		return passwordEncoder.matches(password, checkPassword);
	}
    
	public void updateToken(ReToken token) {
	    int updated = authMapper.updateToken(token);
	    if (updated == 0) {
	        throw new BusinessException(ErrorCode.TOKEN_NOT_FOUND, "Token not found");
	    }
	}
    
    public String getUserId(Integer userNo) {
		return authMapper.getUserId(userNo);
	}
	
	public boolean hasId(String userId) {
		return authMapper.hasId(userId) == 1;
	}
	
	public boolean hasPhone(String phone) {
		return authMapper.hasPhone(phone) == 1;
	}

	public void insertPhoneAuth(PhoneAuthDAO phoneAuth) {
		authMapper.insertPhoneAuth(phoneAuth);
	}
	
	public PhoneAuth getPhoneAuthCode(String phoneAuthToken) {
		return authMapper.getPhoneAuthCode(phoneAuthToken);
	}
	
	public void markPhoneAuthUsed(int phoneAuthId) {
		authMapper.markPhoneAuthUsed(phoneAuthId);
	}
	
	public FindUserDTO getUserByPhone(String phone) {
		return authMapper.getUserByPhone(phone);
	}

	public void joinUser(JoinRequest user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		authMapper.joinUser(user);
		authMapper.joinAddUserAddress(user);
	}

	public void userInfoUpdate(UpdateUserRequest user, Integer userNo) {
	    int updated = authMapper.userInfoUpdate(user, userNo);
	    if (updated == 0) {
	        throw new BusinessException(ErrorCode.USER_NOT_FOUND, "User not found");
	    }
	}
	
	public String getPassword(Integer userNo) {
		return authMapper.getPassword(userNo);
	}
	
	public void changePassword(Integer userNo, String newPassword) {
		authMapper.changePassword(userNo, passwordEncoder.encode(newPassword));
	}
	
	public void insertToken(UserToken token) {
		authMapper.insertToken(token);
	}

	public Integer getUserNoByToken(ReToken token) {
		return authMapper.getUserNoByToken(token);
	}

	public void withDrawalUser(Integer userNo) {
		authMapper.withDrawalUser(userNo);
	}









}
