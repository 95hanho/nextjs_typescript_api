package me._hanho.nextjs_shop.auth;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import me._hanho.nextjs_shop.model.PhoneAuth;

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

	void joinUser(JoinRequest user);

	void joinAddUserAddress(JoinRequest user);
	
	int userInfoUpdate(@Param("u") UpdateUserRequest user, @Param("userNo") Integer userNo);
	
	String getPassword(Integer userNo);
	
	void changePassword(@Param("userNo") Integer userNo, @Param("newPassword") String newPassword);
	
	void insertToken(UserToken token);
	
	int updateToken(ReToken token);

	Integer getUserNoByToken(ReToken token);

	void withDrawalUser(Integer userNo);

	


	

	

	

	
	
}
