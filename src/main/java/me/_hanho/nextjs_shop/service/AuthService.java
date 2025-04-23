package me._hanho.nextjs_shop.service;

import me._hanho.nextjs_shop.model.Token;
import me._hanho.nextjs_shop.model.User;

public interface AuthService {

	User getUser(String user_id);
	
	User getUserExceptPassword(String user_id);
	
	User getUserByToken(Token token);

	void insertToken(Token token);

	void updateToken(Token token);

	boolean getId(String user_id);
	
	void joinMember(User user);

	boolean passwordCheck(String password, String password2);

}
