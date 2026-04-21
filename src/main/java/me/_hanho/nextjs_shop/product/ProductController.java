package me._hanho.nextjs_shop.product;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import me._hanho.nextjs_shop.common.exception.BusinessException;
import me._hanho.nextjs_shop.common.exception.ErrorCode;
import me._hanho.nextjs_shop.model.ProductQnaType;
import me._hanho.nextjs_shop.model.UserCoupon;
import me._hanho.nextjs_shop.product.dto.AddCartRequest;
import me._hanho.nextjs_shop.product.dto.AvailableProductCouponResponse;
import me._hanho.nextjs_shop.product.dto.CartAddResult;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/bapi/product")
public class ProductController {
	
	private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
	
	private final ProductService productService;
	
	// 제품 리스트 조회
	@GetMapping
	public ResponseEntity<Map<String, Object>> getProductList(
			@RequestParam("sort") String sort,
			@RequestParam("menuSubId") int menuSubId, 
			@RequestParam(name = "lastCreatedAt", required = false) Timestamp lastCreatedAt,
			@RequestParam(name = "lastProductId", required = false) Integer lastProductId, 
			@RequestParam(name = "lastPopularity", required = false) Integer lastPopularity) {
		logger.info("[getProductList] sort={}, menuSubId={}, lastCreatedAt={}, lastProductId={}, lastPopularity={}", 
		sort, menuSubId, lastCreatedAt, lastProductId, lastPopularity);
		Map<String, Object> result = new HashMap<String, Object>();
		// 
		List<ProductListResponse> productList = productService.getProductList(sort, menuSubId, lastCreatedAt, lastProductId, lastPopularity);

		result.put("productList", productList);
		result.put("message", "PRODUCT_FETCH_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	// 좋아요/취소
	@PostMapping("like")
	public ResponseEntity<Map<String, Object>> setLike(
			@RequestParam("productId") Integer productId,
			@RequestAttribute(value="userNo", required=false) Integer userNo) {
		if (userNo == null) throw new BusinessException(ErrorCode.AUTHENTICATION_REQUIRED);
		logger.info("[setLike] productId={}, userNo={}", productId, userNo);
		Map<String, Object> result = new HashMap<String, Object>();
		
		productService.setLike(productId, userNo);

		result.put("message", "LIKE_SET_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	// 현재 회원 위시 productId 목록 조회
	@GetMapping("/wish")
	public ResponseEntity<Map<String, Object>> getProductWishList(
			@RequestAttribute(name = "userNo", required = false) Integer userNo) {
		logger.info("[getProductWishList] userNo={}", userNo);
		Map<String, Object> result = new HashMap<String, Object>();

		List<Integer> wishProductIds = productService.getProductWishList(userNo);
		
		result.put("wishProductIds", wishProductIds);
		result.put("message", "PRODUCT_WISH_LIST_FETCH_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	// 위시 등록/해제
	@PostMapping("/wish")
	public ResponseEntity<Map<String, Object>> setWish(
			@RequestParam("productId") Integer productId, 
			@RequestAttribute(value="userNo", required=false) Integer userNo) {
		if (userNo == null) throw new BusinessException(ErrorCode.AUTHENTICATION_REQUIRED);
		logger.info("[setWish] productId={}, userNo={}", productId, userNo);
		Map<String, Object> result = new HashMap<String, Object>();
		
		productService.setWish(productId, userNo);

		result.put("message", "WISH_SET_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	// 장바구니 확인
	@GetMapping("/cart")
	public ResponseEntity<Map<String, Object>> cartCheck(
			@RequestParam("productId") Integer productId, 
			@RequestAttribute(value="userNo", required=false) Integer userNo) {
		if (userNo == null) throw new BusinessException(ErrorCode.AUTHENTICATION_REQUIRED);
		logger.info("[cartCheck] productId={}, userNo={}", productId, userNo);
		Map<String, Object> result = new HashMap<String, Object>();

		boolean hasCart = productService.getProductHasCart(productId, userNo);

		result.put("hasCart", hasCart);
		result.put("message", "CART_CHECK_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	// 장바구니 넣기/수량증가
	@PostMapping("/cart")
	public ResponseEntity<Map<String, Object>> addCart(
			@RequestBody AddCartRequest addCartRequest, 
			@RequestAttribute(value="userNo", required=false) Integer userNo) {
		if (userNo == null) throw new BusinessException(ErrorCode.AUTHENTICATION_REQUIRED);
		logger.info("[addCart] addCartRequest={}, userNo={}", addCartRequest, userNo);
		Map<String, Object> result = new HashMap<String, Object>();
		
		CartAddResult addCartResult = productService.addCart(addCartRequest, userNo);

		if (addCartResult.getSuccessCount() == 0) {
			result.put("message", "CART_ADD_OUT_OF_STOCK");
			return ResponseEntity.status(HttpStatus.CONFLICT).body(result);
		}

		if (addCartResult.isPartial()) {
			result.put("message", "CART_ADD_PARTIAL_SUCCESS");
			result.put("limitedItems", addCartResult.getLimitedItems());
			return ResponseEntity.ok(result);
		}

		result.put("message", "CART_ADD_SUCCESS");
		return ResponseEntity.ok(result);
	}

	// 제품 상세보기 조회
	@GetMapping("/detail/{productId}")
	public ResponseEntity<Map<String, Object>> getProductDetail(
			@PathVariable("productId") int productId,
			@RequestAttribute(name = "userNo", required = false) Integer userNo) {
		logger.info("[getProductDetail] productId={}, userNo={}", productId, userNo);
		Map<String, Object> result = new HashMap<String, Object>();
		
		ProductDetailResponse productDetail = productService.getProductDetail(productId, userNo);
		// 제품 옵션 정보
		List<ProductOptionResponse> productOptionList = productService.getProductOptionList(productId);
		// 리뷰 요약 정보 (평점, 리뷰 수 등)
		ProductReviewSummary productReviewSummary = productService.getProductReviewSummary(productId);
		
		result.put("productDetail", productDetail);
		result.put("productOptionList", productOptionList);
		result.put("productReviewSummary", productReviewSummary);
		result.put("message", "PRODUCT_DETAIL_FETCH_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	// 제품 상세보기 이용가능 쿠폰 조회
	@GetMapping("/detail/{productId}/coupon")
	public ResponseEntity<Map<String, Object>> getProductDetailAvailableCoupon(
			@PathVariable("productId") int productId,
			@RequestAttribute(name = "userNo", required = false) Integer userNo) {
		if (userNo == null) throw new BusinessException(ErrorCode.AUTHENTICATION_REQUIRED);
		logger.info("[getProductDetailAvailableCoupon] productId={}, userNo={}", productId, userNo);
		Map<String, Object> result = new HashMap<String, Object>();
		
		List<AvailableProductCouponResponse> availableProductCoupon = productService.getAvailableProductCoupon(productId, userNo);
		
		result.put("availableProductCoupon", availableProductCoupon);
		result.put("message", "PRODUCT_DETAIL_AVAILABLE_COUPON_FETCH_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	// 제품 상세보기 상세이미지(상품소개) 조회
	@GetMapping("/detail/{productId}/image")
	public ResponseEntity<Map<String, Object>> getProductDetailImage(
			@PathVariable("productId") int productId,
			@RequestAttribute(name = "userNo", required = false) Integer userNo) {
		logger.info("[getProductDetailImage] productId={}, userNo={}", productId, userNo);
		Map<String, Object> result = new HashMap<String, Object>();
		
		List<ProductImageFile> productDetailImageList = productService.getProductDetailImage(productId);
		
		result.put("productDetailImageList", productDetailImageList);
		result.put("message", "PRODUCT_DETAIL_IMAGE_FETCH_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	// 판매자 좋아요 여부 및 판매자 다른 제품 조회
	@GetMapping("/detail/{productId}/seller/like-other-product")
	public ResponseEntity<Map<String, Object>> getSellerLikeAndOtherProducts(
			@PathVariable("productId") int productId,
			@RequestAttribute(name = "userNo", required = false) Integer userNo) {
		logger.info("[getSellerLikeAndOtherProducts] productId={}, userNo={}", productId, userNo);
		Map<String, Object> result = new HashMap<String, Object>();
		
		boolean isSellerLiked = false;
		if(userNo != null) {
			isSellerLiked = productService.isSellerLiked(productId, userNo);
		}
		List<OtherProduct> sellerOtherProducts = productService.getSellerOtherProducts(productId, userNo);
		
		result.put("isSellerLiked", isSellerLiked);
		result.put("sellerOtherProducts", sellerOtherProducts);
		result.put("message", "PRODUCT_SELLER_LIKE_OTHER_PRODUCT_FETCH_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	// 판매자 좋아요/취소
	@PostMapping("/detail/{productId}/seller/like")
	public ResponseEntity<Map<String, Object>> toggleSellerLike(
			@RequestParam("like") Boolean like,
			@PathVariable("productId") int productId,
			@RequestAttribute(name = "userNo", required = false) Integer userNo) {
		if (userNo == null) throw new BusinessException(ErrorCode.AUTHENTICATION_REQUIRED);
		logger.info("[toggleSellerLike] productId={}, userNo={}", productId, userNo);
		Map<String, Object> result = new HashMap<String, Object>();

		productService.setSellerLike(productId, userNo, like);
		
		result.put("message", "PRODUCT_SELLER_LIKE_TOGGLE_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	// 제품 리뷰 조회
	@GetMapping("/detail/{productId}/review")
	public ResponseEntity<Map<String, Object>> getProductReviewList(
			@PathVariable("productId") int productId,
			@RequestAttribute(name = "userNo", required = false) Integer userNo) {
		logger.info("[getProductReviewList] productId={}, userNo={}", productId, userNo);
		Map<String, Object> result = new HashMap<String, Object>();
		
		List<ProductReviewResponse> productReviewList = productService.getProductReviewList(productId, userNo);
		
		result.put("productReviewList", productReviewList);
		result.put("message", "PRODUCT_REVIEW_FETCH_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	// 제품 리뷰 삭제
	@DeleteMapping("/detail/{productId}/review/{reviewId}")
	public ResponseEntity<Map<String, Object>> deleteProductReview(
			@PathVariable("productId") int productId,
			@PathVariable("reviewId") int reviewId,
			@RequestAttribute(name = "userNo", required = false) Integer userNo) {
		if (userNo == null) throw new BusinessException(ErrorCode.AUTHENTICATION_REQUIRED);
		logger.info("[deleteProductReview] productId={}, reviewId={}, userNo={}", productId, reviewId, userNo);
		Map<String, Object> result = new HashMap<String, Object>();
		
		productService.deleteProductReview(reviewId, userNo);
		
		result.put("message", "PRODUCT_REVIEW_DELETE_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	// 제품 상품 Q&A 조회
	@GetMapping("/detail/{productId}/qna")
	public ResponseEntity<Map<String, Object>> getProductQnaList(
			@PathVariable("productId") int productId,
			@RequestAttribute(name = "userNo", required = false) Integer userNo) {
		logger.info("[getProductQnaList] productId={}, userNo={}", productId, userNo);
		Map<String, Object> result = new HashMap<String, Object>();
		
		List<ProductQnaResponse> productQnaList = productService.getProductQnaList(productId, userNo);
		List<ProductQnaType> productQnaTypeList = productService.getProductQnaTypeList();

		result.put("productQnaList", productQnaList);
		result.put("productQnaTypeList", productQnaTypeList);
		result.put("message", "PRODUCT_QNA_FETCH_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	// 제품 상품 Q&A 작성
	@PostMapping("/detail/{productId}/qna")
	public ResponseEntity<Map<String, Object>> createProductQna(
			@PathVariable("productId") int productId,
			@ModelAttribute ProductQnaRequest productQnaRequest,
			@RequestAttribute(name = "userNo", required = false) Integer userNo) {
		if (userNo == null) throw new BusinessException(ErrorCode.AUTHENTICATION_REQUIRED);
		logger.info("[createProductQna] productQnaRequest={}, productId={}, userNo={}", productQnaRequest, productId, userNo);
		Map<String, Object> result = new HashMap<String, Object>();

		productService.createProductQna(productQnaRequest, productId, userNo);
		
		result.put("message", "PRODUCT_QNA_CREATE_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	// 제품 상품 Q&A 수정
	@PutMapping("/detail/{productId}/qna")
	public ResponseEntity<Map<String, Object>> updateProductQna(
			@PathVariable("productId") int productId,
			@ModelAttribute UpdateProductQnaRequest productQnaRequest,
			@RequestAttribute(name = "userNo", required = false) Integer userNo) {
		if (userNo == null) throw new BusinessException(ErrorCode.AUTHENTICATION_REQUIRED);
		logger.info("[updateProductQna] productQnaRequest={}, productId={}, userNo={}", productQnaRequest, productId, userNo);
		Map<String, Object> result = new HashMap<String, Object>();

		productService.updateProductQna(productQnaRequest, userNo);
		
		result.put("message", "PRODUCT_QNA_UPDATE_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	// 제품 상품 Q&A 삭제
	@DeleteMapping("/detail/{productId}/qna/{productQnaId}")
	public ResponseEntity<Map<String, Object>> deleteProductQna(
			@PathVariable("productId") int productId,
			@PathVariable("productQnaId") int productQnaId,
			@RequestAttribute(name = "userNo", required = false) Integer userNo) {
		if (userNo == null) throw new BusinessException(ErrorCode.AUTHENTICATION_REQUIRED);
		logger.info("[deleteProductQna] productQnaId={}, productId={}, userNo={}", productQnaId, productId, userNo);
		Map<String, Object> result = new HashMap<String, Object>();

		productService.deleteProductQna(productQnaId, userNo);
		
		result.put("message", "PRODUCT_QNA_DELETE_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	// 제품 상품 Q&A 답변 읽음 처리
	@PutMapping("/detail/{productId}/qna/read")
	public ResponseEntity<Map<String, Object>> updateProductQnaRead(
			@PathVariable("productId") int productId,
			@RequestParam("productQnaId") Integer productQnaId,
			@RequestAttribute(name = "userNo", required = false) Integer userNo) {
		if (userNo == null) throw new BusinessException(ErrorCode.AUTHENTICATION_REQUIRED);
		logger.info("[updateProductQnaRead] productQnaId={}, productId={}, userNo={}", productQnaId, productId, userNo);
		Map<String, Object> result = new HashMap<String, Object>();

		productService.updateProductQnaRead(productQnaId, userNo);
		
		result.put("message", "PRODUCT_QNA_UPDATE_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	// 같은 카테고리 BEST 제품 조회
	@GetMapping("/detail/{productId}/category-best")
	public ResponseEntity<Map<String, Object>> getCategoryBestProductList(
			@PathVariable("productId") int productId,
			@RequestAttribute(name = "userNo", required = false) Integer userNo) {
		logger.info("[getCategoryBestProductList] productId={}, userNo={}", productId, userNo);
		Map<String, Object> result = new HashMap<String, Object>();
		
		List<OtherProduct> categoryBestProductList = productService.getCategoryBestProductList(productId, userNo);
		
		result.put("categoryBestProductList", categoryBestProductList);
		result.put("message", "CATEGORY_BEST_PRODUCT_FETCH_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	// 쿠폰 다운로드
	@PostMapping("/coupon/download")
	public ResponseEntity<Map<String, Object>> couponDownload(
			@RequestParam("couponId") Integer couponId,
			@RequestAttribute(name = "userNo", required = false) Integer userNo) {
		if (userNo == null) throw new BusinessException(ErrorCode.AUTHENTICATION_REQUIRED);
		logger.info("[couponDownload] couponId={}, userNo={}", couponId, userNo);
		Map<String, Object> result = new HashMap<String, Object>();

		UserCoupon userCoupon = UserCoupon.builder()
				.couponId(couponId)
				.userNo(userNo)
				.build();
		int userCouponId = productService.couponDownload(userCoupon);
		
		if(userCouponId == 0) {
			result.put("message", "COUPON_DOWNLOAD_FAILED");
			return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		result.put("userCouponId", userCouponId);
		result.put("message", "COUPON_DOWNLOAD_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
}
