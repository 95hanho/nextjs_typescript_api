package me._hanho.nextjs_shop.admin;

import org.apache.ibatis.annotations.Mapper;

import me._hanho.nextjs_shop.seller.SellerRegisterRequest;

@Mapper
public interface AdminMapper {
	
	AdminLoginDTO isAdmin(String loginId);

	void addSeller(SellerRegisterRequest seller);

	void setSellerApproval(SellerApprovalRequest sellerApproval);



}
