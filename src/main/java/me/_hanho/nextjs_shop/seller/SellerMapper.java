package me._hanho.nextjs_shop.seller;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import me._hanho.nextjs_shop.auth.dto.ReToken;
import me._hanho.nextjs_shop.model.ProductOption;
import me._hanho.nextjs_shop.seller.dto.AddCouponRequest;
import me._hanho.nextjs_shop.seller.dto.AddFileMeta;
import me._hanho.nextjs_shop.seller.dto.AddProductOptionRequest;
import me._hanho.nextjs_shop.seller.dto.AddProductRequest;
import me._hanho.nextjs_shop.seller.dto.ProductImageResponse;
import me._hanho.nextjs_shop.seller.dto.ProductViewCountResponse;
import me._hanho.nextjs_shop.seller.dto.ProductWishCountResponse;
import me._hanho.nextjs_shop.seller.dto.SellerCouponResponse;
import me._hanho.nextjs_shop.seller.dto.SellerInfoResponse;
import me._hanho.nextjs_shop.seller.dto.SellerLogin;
import me._hanho.nextjs_shop.seller.dto.SellerProductDetailResponse;
import me._hanho.nextjs_shop.seller.dto.SellerProductResponse;
import me._hanho.nextjs_shop.seller.dto.SellerRegisterRequest;
import me._hanho.nextjs_shop.seller.dto.SellerToken;
import me._hanho.nextjs_shop.seller.dto.UpdateCouponRequest;
import me._hanho.nextjs_shop.seller.dto.UpdateFile;
import me._hanho.nextjs_shop.seller.dto.UpdateProductOptionRequest;
import me._hanho.nextjs_shop.seller.dto.UpdateProductRequest;
import me._hanho.nextjs_shop.seller.dto.UserInBookmarkResponse;
import me._hanho.nextjs_shop.seller.dto.UserInCartCountResponse;

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

	void deleteProductImages(@Param("imageIds") List<Integer> imageIds, @Param("sellerNo") Integer sellerNo);

	void updateProductImages(@Param("updateFiles") List<UpdateFile> updateFiles, @Param("sellerNo") Integer sellerNo);

	void insertProductImage(@Param("meta") AddFileMeta meta, @Param("productId") Integer productId, @Param("sellerNo") Integer sellerNo);

	SellerProductDetailResponse getProductDetail(@Param("productId") Integer productId, @Param("sellerNo") Integer sellerNo);

	Integer isProductNameDuplicate(String productName);

	List<ProductImageResponse> getProductImages(Integer productId);

	int addProductOption(@Param("po") AddProductOptionRequest productOption, @Param("sellerNo") Integer sellerNo);

	int updateProductOption(@Param("po") UpdateProductOptionRequest productOption, @Param("sellerNo") Integer sellerNo);
	
	void deleteProductOption(@Param("productOptionId") Integer productOptionId, @Param("sellerNo") Integer sellerNo);
	
	List<SellerCouponResponse> getSellerCouponList(Integer sellerNo);
	
	int countByCouponCode(@Param("couponCode") String couponCode);
	
	void addCoupon(@Param("c") AddCouponRequest coupon, @Param("sellerNo") Integer sellerNo);

	void updateCoupon(@Param("c") UpdateCouponRequest coupon, @Param("sellerNo") Integer sellerNo);
	
	void deleteCoupon(@Param("couponId") Integer couponId, @Param("sellerNo") Integer sellerNo);

	Integer getSellerCouponCountByDescription(@Param("description") String description, @Param("sellerNo") Integer sellerNo);
	
	List<Integer> getProductIdsForCouponAllow(@Param("couponId") Integer couponId, @Param("sellerNo") Integer sellerNo);
	
	void insertSellerCouponAllowList(@Param("couponId") Integer couponId, @Param("productIds") List<Integer> addProductIds, @Param("sellerNo") Integer sellerNo);
	
	void deleteSellerCouponAllowList(@Param("couponId") Integer couponId, @Param("productIds") List<Integer> removeProductIds, @Param("sellerNo") Integer sellerNo);

	void activateCoupons(@Param("couponIds") List<Integer> suspendedCouponIds, @Param("sellerNo") Integer sellerNo);

	void suspendCoupons(@Param("couponIds") List<Integer> activeCouponIds, @Param("sellerNo") Integer sellerNo);
	
	List<ProductViewCountResponse> getProductViewCountList(Integer sellerNo);
	
	List<ProductWishCountResponse> getProductWishCountList(Integer sellerNo);
	
	List<UserInBookmarkResponse> getBrandBookmarkList(Integer sellerNo);

	List<UserInCartCountResponse> getUserInCartCountList(Integer sellerNo);


	

	

	

}
