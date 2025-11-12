package me._hanho.nextjs_shop.product;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import me._hanho.nextjs_shop.model.Cart;
import me._hanho.nextjs_shop.model.Wish;

@RestController
@RequestMapping("/bapi/product")
public class ProductController {
	
	private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
	
	@Autowired
	private ProductService productService;
	
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
	// 위시 등록
	@PostMapping("/wish")
	public ResponseEntity<Map<String, Object>> addToWishList(@ModelAttribute Wish wish) {
		logger.info("getProductList");
		Map<String, Object> result = new HashMap<String, Object>();
		
		productService.addToWishList(wish);

		result.put("message", "success");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	// 위시 등록해제
	@DeleteMapping("/wish/{wishId}")
	public ResponseEntity<Map<String, Object>> deleteWish(@PathVariable("wishId") String wishId) {
		logger.info("deleteWish.wishId : " + wishId);
		Map<String, Object> result = new HashMap<String, Object>();
		
		productService.deleteWish(wishId);

		result.put("message", "success");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	// 장바구니 넣기/수량증가
	@PostMapping("/cart")
	public ResponseEntity<Map<String, Object>> putCart(@ModelAttribute Cart cart) {
		logger.info("putCart");
		Map<String, Object> result = new HashMap<String, Object>();
		
		productService.putCart(cart);

		result.put("message", "success");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	// 제품 상세보기 조회
	
	// 리뷰 조회
	
	// 상품 Q&A
	
	
}
