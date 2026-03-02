package me._hanho.nextjs_shop.product;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import me._hanho.nextjs_shop.common.exception.BusinessException;
import me._hanho.nextjs_shop.common.exception.ErrorCode;

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
		logger.info("getProductList");
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
		logger.info("getProductList");
		Map<String, Object> result = new HashMap<String, Object>();
		
		productService.setLike(productId, userNo);

		result.put("message", "LIKE_SET_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	// 위시 등록/해제
	@PostMapping("/wish")
	public ResponseEntity<Map<String, Object>> setWish(
			@RequestParam("productId") Integer productId, 
			@RequestAttribute(value="userNo", required=false) Integer userNo) {
		if (userNo == null) throw new BusinessException(ErrorCode.AUTHENTICATION_REQUIRED);
		logger.info("setWish productId : " + productId);
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
		logger.info("cartCheck productId : " + productId);
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
		logger.info("addCart" + addCartRequest);
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
		logger.info("getProductDetail productId : " + productId + ", userNo : " + userNo);
		Map<String, Object> result = new HashMap<String, Object>();
		
		ProductDetailResponse productDetail = productService.getProductDetail(productId);
		List<ProductOptionResponse> productOptionList = productService.getProductOptionList(productId);
		ProductReviewSummary productReviewSummary = productService.getProductReviewSummary(productId);
		
		result.put("productDetail", productDetail);
		result.put("productOptionList", productOptionList);
		result.put("productReviewSummary", productReviewSummary);
		result.put("message", "PRODUCT_Detail_FETCH_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	// 제품 상세보기 이용가능 쿠폰 조회
	@GetMapping("/detail/{productId}/coupon")
	public ResponseEntity<Map<String, Object>> getProductDetailAvailableCoupon(
			@PathVariable("productId") int productId,
			@RequestAttribute(name = "userNo", required = false) Integer userNo) {
		if (userNo == null) throw new BusinessException(ErrorCode.AUTHENTICATION_REQUIRED);
		logger.info("getProductDetailAvailableCoupon productId : " + productId + ", userNo : " + userNo);
		Map<String, Object> result = new HashMap<String, Object>();
		
		List<AvailableProductCouponResponse> availableProductCoupon = productService.getAvailableProductCoupon(productId, userNo);
		
		result.put("availableProductCoupon", availableProductCoupon);
		result.put("message", "PRODUCT_Detail_FETCH_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	// 쿠폰 다운로드
	@PostMapping("/coupon/download")
	public ResponseEntity<Map<String, Object>> couponDownload(
			@RequestParam("couponId") Integer couponId,
			@RequestAttribute(name = "userNo", required = false) Integer userNo) {
		if (userNo == null) throw new BusinessException(ErrorCode.AUTHENTICATION_REQUIRED);
		logger.info("couponDownload couponId : " + couponId + ", userNo : " + userNo);
		Map<String, Object> result = new HashMap<String, Object>();
		
		productService.couponDownload(couponId, userNo);
		
		result.put("message", "COUPON_DOWNLOAD_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	// 제품 리뷰 조회
	@GetMapping("/detail/{productId}/review")
	public ResponseEntity<Map<String, Object>> getProductReviewList(
			@PathVariable("productId") int productId,
			@RequestAttribute(name = "userNo", required = false) Integer userNo) {
		logger.info("getProductReviewList productId : " + productId);
		Map<String, Object> result = new HashMap<String, Object>();
		
		List<ProductReviewResponse> productReviewList = productService.getProductReviewList(productId, userNo);
//		ProductReviewSummary productReviewSummary = productService.getProductReviewSummary(productId);
		
//		result.put("productReviewSummary", productReviewSummary);
		result.put("productReviewList", productReviewList);
		result.put("message", "PRODUCT_REVIEW_FETCH_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	// 제품 상품 Q&A 조회
	@GetMapping("/detail/{productId}/qna")
	public ResponseEntity<Map<String, Object>> getProductQnaList(
			@PathVariable("productId") int productId,
			@RequestAttribute(name = "userNo", required = false) Integer userNo) {
		logger.info("getProductQnaList productId : " + productId);
		Map<String, Object> result = new HashMap<String, Object>();
		
		List<ProductQnaResponse> ProductQnaList = productService.getProductQnaList(productId, userNo);
		
		result.put("ProductQnaList", ProductQnaList);
		result.put("message", "PRODUCT_QNA_FETCH_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	
}
