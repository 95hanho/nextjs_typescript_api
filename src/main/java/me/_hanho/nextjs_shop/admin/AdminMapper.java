package me._hanho.nextjs_shop.admin;

import org.apache.ibatis.annotations.Mapper;

import me._hanho.nextjs_shop.auth.TokenDTO;
import me._hanho.nextjs_shop.seller.SellerRegisterRequest;

@Mapper
public interface AdminMapper {
	
	AdminInfo getAdminInfo(Integer adminNo);
	
	AdminLoginDTO isAdmin(String adminId);
	
	void updateLastLoginAt(Integer adminNo);
	
	void insertToken(AdminToken token);

	String getAdminIdByToken(TokenDTO token);
	
	int hasSeller(String sellerId);
	
	void addSeller(SellerRegisterRequest seller);

	void setSellerApproval(SellerApprovalRequest sellerApproval);


	

	

	

	

	

}
