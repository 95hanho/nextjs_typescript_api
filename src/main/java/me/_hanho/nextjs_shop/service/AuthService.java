package me._hanho.nextjs_shop.service;

import me._hanho.nextjs_shop.model.Token;
import me._hanho.nextjs_shop.model.User;

public interface AuthService {

	User getUser(User user);
	
	User getUser(String id);
	
	User getUser(Token token);

	void insertToken(Token token);

	void updateToken(Token token);

	boolean getId(String id);
	
	void joinMember(User user);

	boolean passwordCheck(String password, String password2);

}
