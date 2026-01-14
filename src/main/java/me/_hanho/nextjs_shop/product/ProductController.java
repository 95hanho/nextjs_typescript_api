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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import me._hanho.nextjs_shop.model.Cart;
import me._hanho.nextjs_shop.model.Like;
import me._hanho.nextjs_shop.model.ProductOption;
import me._hanho.nextjs_shop.model.ProductQna;
import me._hanho.nextjs_shop.model.Wish;

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
	public ResponseEntity<Map<String, Object>> setLike(@ModelAttribute Like like, @RequestAttribute("userId") String userId) {
		logger.info("getProductList");
		Map<String, Object> result = new HashMap<String, Object>();
		
		like.setUserId(userId);
		productService.setLike(like);

		result.put("message", "WISH_SET_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	// 위시 등록/해제
	@PostMapping("/wish")
	public ResponseEntity<Map<String, Object>> setWish(@ModelAttribute Wish wish, @RequestAttribute("userId") String userId) {
		logger.info("getProductList");
		Map<String, Object> result = new HashMap<String, Object>();
		
		wish.setUserId(userId);
		productService.setWish(wish);

		result.put("message", "WISH_SET_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	// 장바구니 넣기/수량증가
	@PostMapping("/cart")
	public ResponseEntity<Map<String, Object>> addCart(@ModelAttribute Cart cart, @RequestAttribute("userId") String userId) {
		logger.info("putCart");
		Map<String, Object> result = new HashMap<String, Object>();
		
		cart.setUserId(userId);
		productService.putCart(cart);

		result.put("message", "CART_ADD_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	// 제품 상세보기 조회
	@GetMapping("/detail/{productId}")
	public ResponseEntity<Map<String, Object>> getProductDetail(@PathVariable("productId") int productId,
			@RequestAttribute(name = "userId", required = false) String userId) {
		logger.info("getProductDetail productId : " + productId + ", userId : " + userId);
		Map<String, Object> result = new HashMap<String, Object>();
		
		ProductDetailResponse productDetail = productService.getProductDetail(productId);
		List<ProductOption> productOptionList = productService.getProductOptionList(productId);
		
		List<AvailableProductCouponResponse> availableProductCoupon = null;
		if(userId != null) {
			availableProductCoupon = productService.getAvailableProductCoupon(productId, userId);
		}
		
		result.put("productDetail", productDetail);
		result.put("productOptionList", productOptionList);
		result.put("availableProductCoupon", availableProductCoupon);
		result.put("message", "PRODUCT_Detail_FETCH_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	// 리뷰 조회
	@GetMapping("/review")
	public ResponseEntity<Map<String, Object>> getProductReviewList(@RequestParam("productId") String productId,
			@RequestAttribute(name = "userId", required = false) String userId) {
		logger.info("getProductDetail productId : " + productId);
		Map<String, Object> result = new HashMap<String, Object>();
		
		
		
		result.put("message", "PRODUCT_REVIEW_FETCH_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	// 상품 Q&A
	@GetMapping("/qna")
	public ResponseEntity<Map<String, Object>> getProductQnaList(@RequestParam("productId") String productId,
			@RequestAttribute(name = "userId", required = false) String userId) {
		logger.info("getProductDetail productId : " + productId);
		Map<String, Object> result = new HashMap<String, Object>();
		
		List<ProductQna> ProductQnaList = productService.getProductQnaList(productId, userId);
		
		result.put("ProductQnaList", ProductQnaList);
		result.put("message", "PRODUCT_REVIEW_FETCH_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	
}
