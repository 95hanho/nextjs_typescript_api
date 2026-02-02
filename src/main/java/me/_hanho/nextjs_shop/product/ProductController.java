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
		List<ProductListDTO> productList = productService.getProductList(sort, menuSubId, lastCreatedAt, lastProductId, lastPopularity);

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
	// 장바구니 넣기/수량증가
	@PostMapping("/cart")
	public ResponseEntity<Map<String, Object>> addCart(
			@RequestParam("productOptionId") Integer productOptionId,
			@RequestParam("quantity") Integer quantity,
			@RequestAttribute(value="userNo", required=false) Integer userNo) {
		if (userNo == null) throw new BusinessException(ErrorCode.AUTHENTICATION_REQUIRED);
		logger.info("addCart");
		Map<String, Object> result = new HashMap<String, Object>();
		
		productService.addCart(productOptionId, quantity, userNo);

		result.put("message", "CART_ADD_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	// 제품 상세보기 조회
	@GetMapping("/detail/{productId}")
	public ResponseEntity<Map<String, Object>> getProductDetail(
			@PathVariable("productId") int productId,
			@RequestAttribute(name = "userNo", required = false) Integer userNo) {
		logger.info("getProductDetail productId : " + productId + ", userNo : " + userNo);
		Map<String, Object> result = new HashMap<String, Object>();
		
		ProductDetailResponse productDetail = productService.getProductDetail(productId);
		List<ProductOptionDTO> productOptionList = productService.getProductOptionList(productId);
		
		List<AvailableProductCouponResponse> availableProductCoupon = null;
		if(userNo != null) {
			availableProductCoupon = productService.getAvailableProductCoupon(productId, userNo);
		}
		
		result.put("productDetail", productDetail);
		result.put("productOptionList", productOptionList);
		result.put("availableProductCoupon", availableProductCoupon);
		result.put("message", "PRODUCT_Detail_FETCH_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	// 제품 리뷰 조회
	@GetMapping("/review")
	public ResponseEntity<Map<String, Object>> getProductReviewList(
			@RequestParam("productId") String productId,
			@RequestAttribute(name = "userNo", required = false) Integer userNo) {
		logger.info("getProductReviewList productId : " + productId);
		Map<String, Object> result = new HashMap<String, Object>();
		
		List<ProductReviewResponse> productReviewList = productService.getProductReviewList(productId, userNo);
		
		result.put("productReviewList", productReviewList);
		result.put("message", "PRODUCT_REVIEW_FETCH_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	// 제품 상품 Q&A 조회
	@GetMapping("/qna")
	public ResponseEntity<Map<String, Object>> getProductQnaList(
			@RequestParam("productId") String productId,
			@RequestAttribute(name = "userNo", required = false) Integer userNo) {
		logger.info("getProductQnaList productId : " + productId);
		Map<String, Object> result = new HashMap<String, Object>();
		
		List<ProductQnaResponse> ProductQnaList = productService.getProductQnaList(productId, userNo);
		
		result.put("ProductQnaList", ProductQnaList);
		result.put("message", "PRODUCT_QNA_FETCH_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	
}
