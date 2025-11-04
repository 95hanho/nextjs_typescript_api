package me._hanho.nextjs_shop.auth;

import org.apache.ibatis.annotations.Mapper;

import me._hanho.nextjs_shop.model.Token;
import me._hanho.nextjs_shop.model.User;

@Mapper
public interface AuthMapper {

	User getUser(String userId);
	
	User getUserExceptPassword(String userId);

	User getUserByToken(Token token);
	
	void insertToken(Token token);
	
	void joinUser(User user);
	
	int userInfoUpdate(User user);
	
	int getTokenId(Token token);

	int updateToken(Token token);

	int getId(String userId);
	
	
}
