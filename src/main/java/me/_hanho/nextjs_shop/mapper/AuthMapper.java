package me._hanho.nextjs_shop.mapper;

import org.apache.ibatis.annotations.Mapper;

import me._hanho.nextjs_shop.model.Token;
import me._hanho.nextjs_shop.model.User;

@Mapper
public interface AuthMapper {

	User getUser(User user);
	
	User getUser2(Token token);
	
	User getUser3(String id);

	void insertToken(Token token);
	
	int getToken_id(Token token);

	void updateToken(Token token);

	int getId(String id);
	
	void joinMember(User user);

	
	
}
