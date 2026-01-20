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
	
	void insertToken(@Param("t") SellerToken token, @Param("sellerNo") Integer sellerNo);

	Integer getSellerNoByToken(TokenDTO token);
	
	SellerInfoResponse getSeller(Integer sellerNo);

	int hasId(String sellerId);
	
	void setSeller(SellerRegisterRequest seller);

	List<SellerProductResponse> getSellerProductList(Integer sellerNo);
	
	List<ProductOption> selectDetailsByProductIds(@Param("ids") List<Integer> ids);
	
	void addProduct(@Param("p") AddProductRequest product, @Param("sellerNo") Integer sellerNo);
	
	int updateProduct(@Param("p") UpdateProductRequest product, @Param("sellerNo") Integer sellerNo);

	int addProductOption(@Param("po") AddProductOptionRequest productOption, @Param("sellerNo") Integer sellerNo);

	int updateProductOption(UpdateProductOptionRequest productOption);
	
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
