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

	List<UserResponse> getUserList();

	UserInfoResponse getUserInfoUnmasked(Integer userNo);

	void updateUserWithdrawalStatus(@Param("userNoList") List<Integer> userNoList, @Param("withdrawalStatus") String withdrawalStatus);

	List<CommonCoupon> getCommonCouponList();

	void addCommonCoupon(@Param("c") AddCommonCouponRequest commonCoupon, @Param("adminNo") Integer adminNo);

	void updateCommonCoupon(@Param("c") UpdateCommonCouponRequest commonCoupon);

	void deleteCommonCoupon(@Param("couponId") Integer couponId);

	

	

}
