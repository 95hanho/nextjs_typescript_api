package me._hanho.nextjs_shop.admin;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import me._hanho.nextjs_shop.auth.TokenDTO;
import me._hanho.nextjs_shop.model.Seller;
import me._hanho.nextjs_shop.seller.SellerRegisterRequest;

@Service
@RequiredArgsConstructor
public class AdminService {
	
	private final PasswordEncoder passwordEncoder;
	
	private final AdminMapper adminMapper;
	

	public String getEncryptionPassword(String password) {
		return passwordEncoder.encode(password);
	}
	
	public AdminInfo getAdminInfo(Integer adminNo) {
		return adminMapper.getAdminInfo(adminNo);
	}
	
	public AdminLoginDTO isAdmin(String adminId) {
		return adminMapper.isAdmin(adminId);
	}
	
	public void updateLastLoginAt(Integer adminNo) {
		adminMapper.updateLastLoginAt(adminNo);
	}

    // 패스워드 확인
    public boolean passwordCheck(String password, String checkPassword) {
		return passwordEncoder.matches(password, checkPassword);
	}
    
	public void insertToken(AdminToken token) {
		adminMapper.insertToken(token);
	}
	
	public Integer getAdminNoByToken(TokenDTO token) {
		return adminMapper.getAdminNoByToken(token);
	}
	
	public List<Seller> getSellerList() {
		return adminMapper.getSellerList();
	}
	
	public boolean hasSeller(String sellerId) {
		System.out.println("adminMapper.hasSeller(sellerId) : " +  adminMapper.hasSeller(sellerId));
		return adminMapper.hasSeller(sellerId) == 1;
	}

    
	public void addSeller(SellerRegisterRequest seller, Integer adminNo) {
		seller.setPassword(passwordEncoder.encode(seller.getPassword()));
		adminMapper.addSeller(seller, adminNo);
	}

	public void setSellerApproval(SellerApprovalRequest sellerApproval, Integer adminNo) {
		adminMapper.setSellerApproval(sellerApproval, adminNo);
	}












	
}
