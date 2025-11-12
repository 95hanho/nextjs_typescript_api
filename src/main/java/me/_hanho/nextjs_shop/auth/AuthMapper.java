package me._hanho.nextjs_shop.auth;

import org.apache.ibatis.annotations.Mapper;

import me._hanho.nextjs_shop.model.Token;
import me._hanho.nextjs_shop.model.User;

@Mapper
public interface AuthMapper {

	User getUserExceptPassword(String userId);
	
	User getUser(String userId);
	
	int getId(String userId);

	void joinUser(User user);
	
	int userInfoUpdate(User user);
	
	void insertToken(Token token);
	
	int updateToken(TokenDTO token);

	String getUserIdByToken(TokenDTO token);
	
}
