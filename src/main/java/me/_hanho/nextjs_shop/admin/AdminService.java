package me._hanho.nextjs_shop.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import me._hanho.nextjs_shop.model.Seller;

@Service
public class AdminService {
	
	private final PasswordEncoder passwordEncoder;
	
	@Autowired
	private AdminMapper adminMapper;

	 // 생성자 주입
    public AdminService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
	}
    // 패스워드 확인
    public boolean passwordCheck(String password, String password2) {
		return passwordEncoder.matches(password, password2);
	}
	public void addSeller(Seller seller) {
		seller.setPassword(passwordEncoder.encode(seller.getPassword()));
		adminMapper.addSeller(seller);
	}
	
}
