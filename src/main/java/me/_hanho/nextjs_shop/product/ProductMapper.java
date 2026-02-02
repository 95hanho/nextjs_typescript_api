package me._hanho.nextjs_shop.product;

import java.sql.Timestamp;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import me._hanho.nextjs_shop.mypage.CartOtherOptionResponse;

@Mapper
public interface ProductMapper {

	List<ProductListDTO> getProductList(@Param("sort") String sort, @Param("menuSubId") int menuSubId, @Param("lastCreatedAt") Timestamp lastCreatedAt, 
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

	void addCart(@Param("productOptionId") Integer productOptionId, @Param("quantity") Integer quantity, @Param("userNo") Integer userNo);

	ProductDetailResponse getProductDetail(int productId);
	
	List<ProductImageFile> getProductImageList(int productId);

	List<CartOtherOptionResponse> getProductOptionList(int productId);

	List<AvailableProductCouponResponse> getAvailableProductCoupon(@Param("productId") int productId, @Param("userNo") Integer userNo);
	
	List<ProductReviewResponse> getProductReviewList(@Param("productId") String productId, @Param("userNo") Integer userNo);

	List<ProductQnaResponse> getProductQnaList(@Param("productId") String productId, @Param("userNo") Integer userNo);


	

	

	
}
