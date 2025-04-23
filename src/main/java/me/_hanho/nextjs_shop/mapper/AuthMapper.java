package me._hanho.nextjs_shop.mapper;

import org.apache.ibatis.annotations.Mapper;

import me._hanho.nextjs_shop.model.Token;
import me._hanho.nextjs_shop.model.User;

@Mapper
public interface AuthMapper {

	User getUser(String user_id);
	
	User getUserExceptPassword(String user_id);

	User getUserByToken(Token token);
	
	void insertToken(Token token);
	
	int getToken_id(Token token);

	void updateToken(Token token);

	int getId(String user_id);
	
	void joinMember(User user);

	
	
}
