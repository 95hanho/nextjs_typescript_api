package me._hanho.nextjs_shop.admin;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import me._hanho.nextjs_shop.seller.SellerRegisterRequest;

@Service
@RequiredArgsConstructor
public class AdminService {
	
	private final PasswordEncoder passwordEncoder;
	
	private final AdminMapper adminMapper;
	
	public AdminLoginDTO isAdmin(String loginId) {
		return adminMapper.isAdmin(loginId);
	}

    // 패스워드 확인
    public boolean passwordCheck(String password, String checkPassword) {
		return passwordEncoder.matches(password, checkPassword);
	}
    
	public void addSeller(SellerRegisterRequest seller) {
		seller.setPassword(passwordEncoder.encode(seller.getPassword()));
		adminMapper.addSeller(seller);
	}

	public void setSellerApproval(SellerApprovalRequest sellerApproval) {
		adminMapper.setSellerApproval(sellerApproval);
	}


	
}
