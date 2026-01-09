package me._hanho.nextjs_shop.admin;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import me._hanho.nextjs_shop.model.Seller;

@Service
@RequiredArgsConstructor
public class AdminService {
	
	private final PasswordEncoder passwordEncoder;
	
	private final AdminMapper adminMapper;

    // 패스워드 확인
    public boolean passwordCheck(String password, String password2) {
		return passwordEncoder.matches(password, password2);
	}
    
	public void addSeller(Seller seller) {
		seller.setPassword(passwordEncoder.encode(seller.getPassword()));
		adminMapper.addSeller(seller);
	}
	
}
