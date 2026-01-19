package me._hanho.nextjs_shop.auth;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import me._hanho.nextjs_shop.model.PhoneAuth;
import me._hanho.nextjs_shop.model.User;

@Mapper
public interface AuthMapper {

	UserInfo getUserInfo(Integer userNo);
	
	UserLoginResponse getUserForPassword(String userId);
	
	String getUserId(Integer userNo);
	
	int hasId(String userId);
	
	void insertPhoneAuth(PhoneAuthDAO phoneAuth);
	
	PhoneAuth getPhoneAuthCode(String phoneAuthToken);

	void markPhoneAuthUsed(int phoneAuthId);
	
	String getUserIdByPhone(String phone);

	void joinUser(User user);
	
	int userInfoUpdate(UpdateUserRequest user);
	
	void changePassword(@Param("userId") String userId, @Param("newPassword") String newPassword);
	
	void insertToken(TokenDTO token);
	
	int updateToken(TokenDTO token);

	Integer getUserNoByToken(TokenDTO token);

	void withDrawalUser(Integer userNo);

	

	

	

	
	
}
