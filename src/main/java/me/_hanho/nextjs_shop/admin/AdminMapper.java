package me._hanho.nextjs_shop.admin;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import me._hanho.nextjs_shop.auth.TokenDTO;
import me._hanho.nextjs_shop.model.Seller;
import me._hanho.nextjs_shop.seller.SellerRegisterRequest;

@Mapper
public interface AdminMapper {
	
	AdminInfo getAdminInfo(Integer adminNo);
	
	AdminLoginDTO isAdmin(String adminId);
	
	void updateLastLoginAt(Integer adminNo);
	
	void insertToken(AdminToken token);

	Integer getAdminNoByToken(TokenDTO token);
	
	List<Seller> getSellerList();
	
	int hasSeller(String sellerId);
	
	void addSeller(@Param("s") SellerRegisterRequest seller, @Param("adminNo") Integer adminNo);

	void setSellerApproval(@Param("s") SellerApprovalRequest sellerApproval, @Param("adminNo") Integer adminNo);

	


	

	

	

	

	

}
