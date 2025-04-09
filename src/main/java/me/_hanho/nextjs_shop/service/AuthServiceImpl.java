package me._hanho.nextjs_shop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import me._hanho.nextjs_shop.model.Token;
import me._hanho.nextjs_shop.model.User;
import me._hanho.nextjs_shop.repository.AuthRepository;

@Service
public class AuthServiceImpl implements AuthService {

	private final PasswordEncoder passwordEncoder;
	
	 // 생성자 주입
    public AuthServiceImpl(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
	}
	
	@Autowired
	private AuthRepository authDAO;
	
	@Override
	public User getUser(User user) {
		return authDAO.getUser(user);
	}
	
	@Override
	public User getUser(String id) {
		return authDAO.getUser(id);
	}
	
	@Override
	public User getUser(Token token) {
		return authDAO.getUser(token);
	}

	@Override
	public void insertToken(Token token) {
		authDAO.insertToken(token);
	}

	@Override
	public void updateToken(Token token) {
		authDAO.updateToken(token);
	}

	@Override
	public boolean getId(String id) {
		return authDAO.getId(id);
	}
	
	@Override
	public void joinMember(User user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		authDAO.joinMember(user);
	}

	@Override
	public boolean passwordCheck(String password, String password2) {
		return passwordEncoder.matches(password, password2);
	}
	
}
