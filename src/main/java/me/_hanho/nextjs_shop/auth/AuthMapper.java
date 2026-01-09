package me._hanho.nextjs_shop.auth;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import me._hanho.nextjs_shop.model.PhoneAuth;
import me._hanho.nextjs_shop.model.Token;
import me._hanho.nextjs_shop.model.User;

@Mapper
public interface AuthMapper {

	User getUserExceptPassword(String userId);
	
	User getUser(String userId);
	
	int getId(String userId);
	
	void insertPhoneAuth(PhoneAuth phoneAuth);
	
	PhoneAuth getPhoneAuthCode(String phoneAuthToken);

	void markPhoneAuthUsed(int phoneAuthId);

	void joinUser(User user);
	
	int userInfoUpdate(User user);
	
	void changePassword(@Param("userId") String userId, @Param("newPassword") String newPassword);
	
	void insertToken(Token token);
	
	int updateToken(TokenDTO token);

	String getUserIdByToken(TokenDTO token);

	

	
	
}
