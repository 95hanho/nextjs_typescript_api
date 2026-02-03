package me._hanho.nextjs_shop.seller;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import me._hanho.nextjs_shop.auth.ReToken;
import me._hanho.nextjs_shop.model.ProductOption;

@Mapper
public interface SellerMapper {
	
	SellerLogin isSeller(String sellerId);
	
	void insertToken(@Param("t") SellerToken token, @Param("sellerNo") Integer sellerNo);

	Integer getSellerNoByToken(ReToken token);
	
	SellerInfoResponse getSeller(Integer sellerNo);

	int hasId(String sellerId);
	
	void setSeller(SellerRegisterRequest seller);

	List<SellerProductResponse> getSellerProductList(Integer sellerNo);
	
	List<ProductOption> selectDetailsByProductIds(@Param("ids") List<Integer> ids);
	
	void addProduct(@Param("p") AddProductRequest product, @Param("sellerNo") Integer sellerNo);
	
	int updateProduct(@Param("p") UpdateProductRequest product, @Param("sellerNo") Integer sellerNo);

	int addProductOption(@Param("po") AddProductOptionRequest productOption, @Param("sellerNo") Integer sellerNo);

	int updateProductOption(@Param("po") UpdateProductOptionRequest productOption, @Param("sellerNo") Integer sellerNo);
	
	void deleteProductOption(@Param("productOptionId") Integer productOptionId, @Param("sellerNo") Integer sellerNo);
	
	List<SellerCouponResponse> getSellerCouponList(Integer sellerNo);
	
	int countByCouponCode(@Param("couponCode") String couponCode);
	
	void addCoupon(@Param("c") AddCouponRequest coupon, @Param("sellerNo") Integer sellerNo);

	void updateCoupon(@Param("c") UpdateCouponRequest coupon, @Param("sellerNo") Integer sellerNo);
	
	void deleteCoupon(@Param("couponId") Integer couponId, @Param("sellerNo") Integer sellerNo);
	
	List<SellerProductCouponAllowedResponse> getSellerCouponAllow(@Param("couponId") String couponId, @Param("sellerNo") Integer sellerNo);
	
	void insertSellerCouponAllowList(@Param("couponId") String couponId, @Param("productIds") List<Integer> productIds, @Param("sellerNo") Integer sellerNo);
	
	void deleteSellerCouponAllowList(@Param("couponId") String couponId, @Param("productIds") List<Integer> productIds, @Param("sellerNo") Integer sellerNo);
	
	List<ProductViewCountResponse> getProductViewCountList(Integer sellerNo);
	
	List<ProductWishCountResponse> getProductWishCountList(Integer sellerNo);
	
	List<UserInBookmarkResponse> getBrandBookmarkList(Integer sellerNo);

	List<UserInCartCountResponse> getUserInCartCountList(Integer sellerNo);


	

	

	

}
