package me._hanho.nextjs_shop.product;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ProductMapper {

	List<ProductListResponse> getProductList(@Param("sort") String sort, @Param("menuSubId") int menuSubId, @Param("lastCreatedAt") Timestamp lastCreatedAt, 
			@Param("lastProductId") Integer lastProductId, @Param("lastPopularity") Integer lastPopularity);
	
	List<ProductImageFile> getProductImageListByProductIds(List<Integer> productIds);
	
	boolean isLikeExist(@Param("productId") Integer productId, @Param("userNo") Integer userNo);

	void upProductLike(Integer productId);

	void insertLike(@Param("productId") Integer productId, @Param("userNo") Integer userNo);

	void downProductLike(Integer productId);

	void deleteLike(@Param("productId") Integer productId, @Param("userNo") Integer userNo);
	
	boolean isWishExist(@Param("productId") Integer productId, @Param("userNo") Integer userNo);
	
	void upProductWish(Integer productId);
	
	void insertWish(@Param("productId") Integer productId, @Param("userNo") Integer userNo);
	
	void downProductWish(Integer productId);

	void deleteWish(@Param("productId") Integer productId, @Param("userNo") Integer userNo);
	//
	List<Integer> getProductCart(@Param("productId") Integer productId, @Param("userNo") Integer userNo);

	List<CartQtyRow> getCartQtyMap(@Param("userNo") Integer userNo, @Param("cartList") List<AddCartItem> cartList);

	int upQuantityCart(@Param("updateList") List<AddCartItem> updateList, @Param("userNo") Integer userNo);

	int addCart(@Param("insertList") List<AddCartItem> insertList, @Param("userNo") Integer userNo);

	List<CartAppliedRow> getCartAppliedResult(@Param("userNo") Integer userNo, @Param("cartList") List<AddCartItem> cartList);

	ProductDetailResponse getProductDetail(int productId);
	
	List<ProductImageFile> getProductImageList(int productId);

	List<ProductOptionResponse> getProductOptionList(int productId);

	List<AvailableProductCouponResponse> getAvailableProductCoupon(@Param("productId") int productId, @Param("userNo") Integer userNo);
	
	void couponDownload(@Param("couponId") Integer couponId, @Param("userNo") Integer userNo);

	List<ProductReviewResponse> getProductReviewList(@Param("productId") Integer productId, @Param("userNo") Integer userNo);
	
	ProductReviewSummary getProductReviewSummary(Integer productId);
	
	List<ProductQnaResponse> getProductQnaList(@Param("productId") int productId, @Param("userNo") Integer userNo);

	
}
