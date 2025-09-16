package me._hanho.nextjs_shop.seller;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import me._hanho.nextjs_shop.model.BrandBookmark;
import me._hanho.nextjs_shop.model.Coupon;
import me._hanho.nextjs_shop.model.Product;
import me._hanho.nextjs_shop.model.ProductDetail;

@Mapper
public interface SellerMapper {

	List<SellerProductDTO> getSellerProductList(String seller_id);
	
	List<ProductDetail> selectDetailsByProductIds(@Param("ids") List<Integer> ids);
	
	void addProduct(Product product);
	
	int updateProduct(Product product);

	void addProductDetail(ProductDetail productDetail);

	int updateProductDetail(ProductDetail productDetail);
	
	List<Coupon> getSellerCouponList(String seller_id);
	
	void addCoupon(Coupon coupon);

	void updateCouponStatus(Coupon coupon);

	List<SellerCouponAllowedProductDTO> getSellerCouponAllow(String coupon_id);
	
	void deleteAllsellerCouponAllow(String coupon_id);
	
	void insertSellerCouponAllowList(@Param("coupon_id") String coupon_id, @Param("productIds") List<Integer> productIds);
	
	void issueCouponsToUsers(@Param("coupon_id") String coupon_id, @Param("userIds") List<String> userIds);
	
	List<ProductViewCountDTO> getProductViewCountList(String seller_id);
	
	List<ProductWishCountDTO> getProductWishCountList(String seller_id);
	
	List<userInBookmarkDTO> getBrandBookmarkList(String seller_id);

	List<UserInCartCountDTO> getUserInCartCountList(String seller_id);

	

}
