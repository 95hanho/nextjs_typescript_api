package me._hanho.nextjs_shop.admin;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import me._hanho.nextjs_shop.auth.ReToken;
import me._hanho.nextjs_shop.common.util.MaskingUtil;
import me._hanho.nextjs_shop.model.Seller;
import me._hanho.nextjs_shop.seller.SellerRegisterRequest;
import me._hanho.nextjs_shop.seller.SellerService;

@Service
@RequiredArgsConstructor
public class AdminService {
	
	private final PasswordEncoder passwordEncoder;
	private final SellerService sellerService;
	
	private final AdminMapper adminMapper;
	

	public String getEncryptionPassword(String password) {
		return passwordEncoder.encode(password);
	}
	
	public AdminInfoResponse getAdminInfo(Integer adminNo) {
		return adminMapper.getAdminInfo(adminNo);
	}
	
	public AdminLogin isAdmin(String adminId) {
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
	
	public Integer getAdminNoByToken(ReToken token) {
		return adminMapper.getAdminNoByToken(token);
	}
	
	public List<Seller> getSellerList() {
		List<Seller> sellerList = adminMapper.getSellerList();
		
		sellerList.forEach(seller -> {
			seller.setSellerId(MaskingUtil.maskUserIdName(seller.getSellerId(), 5));
		});
		
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

	public List<UserResponse> getUserList() {
		List<UserResponse> userList = adminMapper.getUserList();
		
		userList.forEach(user -> {
			user.setUserId(MaskingUtil.maskUserIdName(user.getUserId(), 5));
			user.setName(MaskingUtil.maskUserIdName(user.getName(), 3));
			user.setPhone(MaskingUtil.maskPhone(user.getPhone()));
			user.setEmail(MaskingUtil.maskEmail(user.getEmail(), 5));
		});
		
		return userList;
	}

	public UserInfoInAdminResponse getUserInfoUnmasked(Integer userNo) {
		return adminMapper.getUserInfoUnmasked(userNo);
	}
	@Transactional
	public void updateUserWithdrawalStatus(List<Integer> userNoList, String withdrawalStatus) {
		adminMapper.updateUserWithdrawalStatus(userNoList, withdrawalStatus);
	}

	public List<CommonCouponResponse> getCommonCouponList() {
		return adminMapper.getCommonCouponList();
	}

	public void addCommonCoupon(AddCommonCouponRequest commonCoupon, Integer adminNo) {
		commonCoupon.setCouponCode(sellerService.generateUniqueCouponCode());
		adminMapper.addCommonCoupon(commonCoupon, adminNo);
	}

	public void updateCommonCoupon(UpdateCommonCouponRequest commonCoupon) {
		adminMapper.updateCommonCoupon(commonCoupon);
	}

	public void deleteCommonCoupon(Integer couponId) {
		adminMapper.deleteCommonCoupon(couponId);
	}



	
}
