package me._hanho.nextjs_shop.product;

import java.sql.Timestamp;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import me._hanho.nextjs_shop.model.Cart;
import me._hanho.nextjs_shop.model.Like;
import me._hanho.nextjs_shop.model.ProductOption;
import me._hanho.nextjs_shop.model.ProductQna;
import me._hanho.nextjs_shop.model.Wish;

@Mapper
public interface ProductMapper {

	List<ProductListDTO> getProductList(@Param("sort") String sort, @Param("menuSubId") int menuSubId, @Param("lastCreatedAt") Timestamp lastCreatedAt, 
			@Param("lastProductId") Integer lastProductId, @Param("lastPopularity") Integer lastPopularity);
	
	List<ProductImageFile> getProductImageListByProductIds(List<Integer> productIds);
	
	boolean isWishExist(Wish wish);
	
	void upProductWish(int productId);
	
	void insertWish(Wish wish);
	
	void downProductWish(int productId);

	void deleteWish(Wish wish);
	//
	boolean isLikeExist(Like like);

	void upProductLike(int productId);

	void insertLike(Like like);

	void downProductLike(int productId);

	void deleteLike(Like like);
	
	void putCart(Cart cart);

	ProductDetailResponse getProductDetail(int productId);
	
	List<ProductImageFile> getProductImageList(int productId);

	List<ProductOption> getProductOptionList(int productId);

	List<AvailableProductCouponResponse> getAvailableProductCoupon(@Param("productId") int productId, @Param("userId") String userId);
	
	List<ProductReviewResponse> getProductReviewList(@Param("productId") String productId, @Param("userId")  String userId);

	List<ProductQna> getProductQnaList(@Param("productId") String productId, @Param("userId") String userId);

	

	

	
}
