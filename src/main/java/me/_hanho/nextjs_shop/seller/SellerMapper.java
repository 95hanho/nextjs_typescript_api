package me._hanho.nextjs_shop.seller;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import me._hanho.nextjs_shop.auth.TokenDTO;
import me._hanho.nextjs_shop.model.Coupon;
import me._hanho.nextjs_shop.model.Product;
import me._hanho.nextjs_shop.model.ProductOption;
import me._hanho.nextjs_shop.model.Token;

@Mapper
public interface SellerMapper {
	
	SellerLoginDTO isSeller(String sellerId);
	
	void insertToken(Token token);

	String getSellerIdByToken(TokenDTO token);
	
	SellerInfoResponse getSeller(String sellerId);
	
	void setSeller(SellerRegisterRequest seller);

	List<SellerProductDTO> getSellerProductList(String sellerId);
	
	List<ProductOption> selectDetailsByProductIds(@Param("ids") List<Integer> ids);
	
	void addProduct(Product product);
	
	int updateProduct(Product product);

	void addProductOption(ProductOption productOption);

	int updateProductOption(ProductOption productOption);
	
	List<Coupon> getSellerCouponList(String sellerId);
	
	void addCoupon(Coupon coupon);

	void updateCouponStatus(Coupon coupon);

	List<SellerCouponAllowedProductDTO> getSellerCouponAllow(String couponId);
	
	void deleteAllsellerCouponAllow(String couponId);
	
	void insertSellerCouponAllowList(@Param("couponId") String couponId, @Param("productIds") List<Integer> productIds);
	
	void issueCouponsToUsers(@Param("couponId") String couponId, @Param("userIds") List<String> userIds);
	
	List<ProductViewCountDTO> getProductViewCountList(String sellerId);
	
	List<ProductWishCountDTO> getProductWishCountList(String sellerId);
	
	List<userInBookmarkDTO> getBrandBookmarkList(String sellerId);

	List<UserInCartCountDTO> getUserInCartCountList(String sellerId);


	
	

	

	

}
