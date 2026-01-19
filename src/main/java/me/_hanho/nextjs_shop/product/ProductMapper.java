package me._hanho.nextjs_shop.product;

import java.sql.Timestamp;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import me._hanho.nextjs_shop.model.ProductOption;

@Mapper
public interface ProductMapper {

	List<ProductListDTO> getProductList(@Param("sort") String sort, @Param("menuSubId") int menuSubId, @Param("lastCreatedAt") Timestamp lastCreatedAt, 
			@Param("lastProductId") Integer lastProductId, @Param("lastPopularity") Integer lastPopularity);
	
	List<ProductImageFile> getProductImageListByProductIds(List<Integer> productIds);
	
	boolean isWishExist(AddWishRequest wish);
	
	void upProductWish(int productId);
	
	void insertWish(AddWishRequest wish);
	
	void downProductWish(int productId);

	void deleteWish(AddWishRequest wish);
	//
	boolean isLikeExist(AddLikeRequest like);

	void upProductLike(int productId);

	void insertLike(AddLikeRequest like);

	void downProductLike(int productId);

	void deleteLike(AddLikeRequest like);
	
	void addCart(AddCartRequest cart);

	ProductDetailResponse getProductDetail(int productId);
	
	List<ProductImageFile> getProductImageList(int productId);

	List<ProductOption> getProductOptionList(int productId);

	List<AvailableProductCouponResponse> getAvailableProductCoupon(@Param("productId") int productId, @Param("userNo") Integer userNo);
	
	List<ProductReviewResponse> getProductReviewList(@Param("productId") String productId, @Param("userNo") Integer userNo);

	List<ProductQnaResponse> getProductQnaList(@Param("productId") String productId, @Param("userNo") Integer userNo);

	

	

	
}
