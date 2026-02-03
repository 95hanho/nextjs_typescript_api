package me._hanho.nextjs_shop.admin;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import me._hanho.nextjs_shop.auth.ReToken;
import me._hanho.nextjs_shop.model.Seller;
import me._hanho.nextjs_shop.seller.SellerRegisterRequest;

@Mapper
public interface AdminMapper {
	
	AdminInfoResponse getAdminInfo(Integer adminNo);
	
	AdminLogin isAdmin(String adminId);
	
	void updateLastLoginAt(Integer adminNo);
	
	void insertToken(AdminToken token);

	Integer getAdminNoByToken(ReToken token);
	
	List<Seller> getSellerList();
	
	int hasSeller(String sellerId);
	
	void addSeller(@Param("s") SellerRegisterRequest seller, @Param("adminNo") Integer adminNo);

	void setSellerApproval(@Param("s") SellerApprovalRequest sellerApproval, @Param("adminNo") Integer adminNo);

	List<UserResponse> getUserList();

	UserInfoInAdminResponse getUserInfoUnmasked(Integer userNo);

	void updateUserWithdrawalStatus(@Param("userNoList") List<Integer> userNoList, @Param("withdrawalStatus") String withdrawalStatus);
	
	List<CommonCouponResponse> getCommonCouponList();

	void addCommonCoupon(@Param("c") AddCommonCouponRequest commonCoupon, @Param("adminNo") Integer adminNo);

	void updateCommonCoupon(@Param("c") UpdateCommonCouponRequest commonCoupon);

	void deleteCommonCoupon(@Param("couponId") Integer couponId);

	

	

	

}
