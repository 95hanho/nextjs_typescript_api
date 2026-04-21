package me._hanho.nextjs_shop.product;

import java.sql.Timestamp;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import me._hanho.nextjs_shop.model.ProductQnaType;
import me._hanho.nextjs_shop.model.UserCoupon;
import me._hanho.nextjs_shop.mypage.dto.ReviewImageResponse;
import me._hanho.nextjs_shop.product.dto.AddCartItem;
import me._hanho.nextjs_shop.product.dto.AvailableProductCouponResponse;
import me._hanho.nextjs_shop.product.dto.CartAppliedRow;
import me._hanho.nextjs_shop.product.dto.CartQtyRow;
import me._hanho.nextjs_shop.product.dto.OtherProduct;
import me._hanho.nextjs_shop.product.dto.ProductDetailResponse;
import me._hanho.nextjs_shop.product.dto.ProductImageFile;
import me._hanho.nextjs_shop.product.dto.ProductListResponse;
import me._hanho.nextjs_shop.product.dto.ProductOptionResponse;
import me._hanho.nextjs_shop.product.dto.ProductQnaRequest;
import me._hanho.nextjs_shop.product.dto.ProductQnaResponse;
import me._hanho.nextjs_shop.product.dto.ProductReviewResponse;
import me._hanho.nextjs_shop.product.dto.ProductReviewSummary;
import me._hanho.nextjs_shop.product.dto.UpdateProductQnaRequest;

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
	
	List<Integer> getProductWishList(Integer userNo);

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

	void upProductHit(int productId);

	void insertProductView(@Param("productId") int productId, @Param("userNo") int userNo);
	
	List<ProductImageFile> getProductImageList(int productId);

	List<ProductOptionResponse> getProductOptionList(int productId);

	List<AvailableProductCouponResponse> getAvailableProductCoupon(@Param("productId") int productId, @Param("userNo") Integer userNo);
	
	List<ProductImageFile> getProductDetailImage(int productId);

	Integer isSellerLikeExist(@Param("productId") Integer productId, @Param("userNo") Integer userNo);

	List<OtherProduct> getSellerOtherProducts(@Param("productId") int productId, @Param("userNo") Integer userNo);

	void upSellerLike(int productId);

	void insertSellerLike(@Param("productId") int productId, @Param("userNo") Integer userNo);

	void downSellerLike(int productId);
	
	void deleteSellerLike(@Param("productId") int productId, @Param("userNo") Integer userNo);

	List<ProductReviewResponse> getProductReviewList(@Param("productId") Integer productId, @Param("userNo") Integer userNo);

	void deleteProductReview(@Param("reviewId") int reviewId, @Param("userNo") Integer userNo);

	List<ReviewImageResponse> getProductReviewImageListByProductId(Integer productId);
	
	ProductReviewSummary getProductReviewSummary(Integer productId);
	
	List<ProductQnaResponse> getProductQnaList(@Param("productId") int productId, @Param("userNo") Integer userNo);

	List<ProductQnaType> getProductQnaTypeList();

	void insertProductQna(@Param("qna") ProductQnaRequest productQnaRequest, @Param("productId") Integer productId, @Param("userNo") Integer userNo);

	void updateProductQna(@Param("qna") UpdateProductQnaRequest productQnaRequest, @Param("userNo") Integer userNo);

	void updateProductQnaDelete(@Param("productQnaId") int productQnaId, @Param("userNo") Integer userNo);

	void updateProductQnaRead(@Param("productQnaId") int productQnaId, @Param("userNo") Integer userNo);

	List<OtherProduct> getCategoryBestProductList(@Param("productId") int productId, @Param("userNo") Integer userNo);

	int couponDownload(UserCoupon userCoupon);
	
}
