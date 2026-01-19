package me._hanho.nextjs_shop.seller;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import me._hanho.nextjs_shop.auth.TokenDTO;
import me._hanho.nextjs_shop.model.Coupon;
import me._hanho.nextjs_shop.model.ProductOption;

@Mapper
public interface SellerMapper {
	
	SellerLoginDTO isSeller(String sellerId);
	
	void insertToken(SellerToken token);

	Integer getSellerNoByToken(TokenDTO token);
	
	SellerInfoResponse getSeller(Integer sellerNo);
	
	void setSeller(SellerRegisterRequest seller);

	List<SellerProductDTO> getSellerProductList(Integer sellerNo);
	
	List<ProductOption> selectDetailsByProductIds(@Param("ids") List<Integer> ids);
	
	void addProduct(AddProductRequest product);
	
	int updateProduct(UpdateProductRequest product);

	void addProductOption(ProductOption productOption);

	int updateProductOption(ProductOption productOption);
	
	List<Coupon> getSellerCouponList(Integer sellerNo);
	
	void addCoupon(Coupon coupon);

	void updateCouponStatus(Coupon coupon);

	List<SellerCouponAllowedProductDTO> getSellerCouponAllow(String couponId);
	
	void deleteAllsellerCouponAllow(String couponId);
	
	void insertSellerCouponAllowList(@Param("couponId") String couponId, @Param("productIds") List<Integer> productIds);
	
	void issueCouponsToUsers(@Param("couponId") String couponId, @Param("userIds") List<String> userIds);
	
	List<ProductViewCountDTO> getProductViewCountList(Integer sellerNo);
	
	List<ProductWishCountDTO> getProductWishCountList(Integer sellerNo);
	
	List<userInBookmarkDTO> getBrandBookmarkList(Integer sellerNo);

	List<UserInCartCountDTO> getUserInCartCountList(Integer sellerNo);


	
	

	

	

}
