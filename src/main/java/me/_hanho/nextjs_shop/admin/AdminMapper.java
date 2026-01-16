package me._hanho.nextjs_shop.admin;

import org.apache.ibatis.annotations.Mapper;

import me._hanho.nextjs_shop.auth.TokenDTO;
import me._hanho.nextjs_shop.model.Token;
import me._hanho.nextjs_shop.seller.SellerRegisterRequest;

@Mapper
public interface AdminMapper {
	
	AdminInfo getAdminInfo(int adminNo);
	
	AdminLoginDTO isAdmin(String adminId);
	
	void updateLastLoginAt(int adminNo);
	
	void insertToken(Token token);

	String getAdminIdByToken(TokenDTO token);
	
	int hasSeller(String sellerId);
	
	void addSeller(SellerRegisterRequest seller);

	void setSellerApproval(SellerApprovalRequest sellerApproval);


	

	

	

	

	

}
