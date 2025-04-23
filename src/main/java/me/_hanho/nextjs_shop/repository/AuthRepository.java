package me._hanho.nextjs_shop.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import me._hanho.nextjs_shop.mapper.AuthMapper;
import me._hanho.nextjs_shop.model.Token;
import me._hanho.nextjs_shop.model.User;

@Repository
public class AuthRepository {

	@Autowired
	private AuthMapper authMapper;
	
	public User getUser(String user_id) {
		return authMapper.getUser(user_id);
	}
	
	public User getUserExceptPassword(String user_id) {
		return authMapper.getUserExceptPassword(user_id);
	}
	
	public User getUserByToken(Token token) {
		return authMapper.getUserByToken(token);
	}

	public void insertToken(Token token) {
		authMapper.insertToken(token);
	}

	public void updateToken(Token token) {
		int token_id = authMapper.getToken_id(token);
		token.setToken_id(token_id);
		System.out.println(token);
		authMapper.updateToken(token);
	}

	public boolean getId(String user_id) {
		System.out.println(authMapper.getId(user_id));
		return authMapper.getId(user_id) == 1;
	}
	
	public void joinMember(User user) {
		authMapper.joinMember(user);
	}


}
