package me._hanho.nextjs_shop.seller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import me._hanho.nextjs_shop.model.BrandBookmark;
import me._hanho.nextjs_shop.model.Cart;
import me._hanho.nextjs_shop.model.Coupon;
import me._hanho.nextjs_shop.model.Product;
import me._hanho.nextjs_shop.model.ProductDetail;
import me._hanho.nextjs_shop.model.User;
import me._hanho.nextjs_shop.product.ProductController;

@RestController
@RequestMapping("/bapi/seller")
public class SellerController {
	
	private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
	
	@Autowired
	private SellerService sellerService;
	
	// 판매자 제품 조회
	@GetMapping("/product")
	public ResponseEntity<Map<String, Object>> getSellerProductList(@RequestParam("seller_id") String seller_id) {
		logger.info("getSellerProductList");
		Map<String, Object> result = new HashMap<String, Object>();
		
		List<SellerProductDTO> sellerProductList = sellerService.getSellerProductList(seller_id);
		
		result.put("sellerProductList", sellerProductList);
		result.put("msg", "success");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	// 제품 추가 / 수정
	@PostMapping("/product")
	public ResponseEntity<Map<String, Object>> addProduct(@ModelAttribute Product product) {
		logger.info("sellerAddProduct " + product);
		Map<String, Object> result = new HashMap<String, Object>();

		if(product.getProduct_id() == 0) {
			sellerService.addProduct(product);
		} else {
			sellerService.updateProduct(product);
		}

		result.put("msg", "success");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	// 판매자 제품 상세보기
	@GetMapping("/product/detail")
	public ResponseEntity<Map<String, Object>> getSellerProductDetail(@RequestParam("product_id") String product_id) {
		logger.info("getSellerProductList");
		Map<String, Object> result = new HashMap<String, Object>();
		
		// 실제리스트에 뜨는 예시도 보여주고
		// 해당 리뷰 보여주고
		// 해당 QnA 보여주고
		// 판매자 제품에 적용 가능한 쿠폰조회 getProductAvailableCoupons

//		result.put("sellerProductList", sellerProductList);
		result.put("msg", "success");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	// 판매자 제품 상세 추가 / 수정
	@PostMapping("/product/detail")
	public ResponseEntity<Map<String, Object>> setSellerProductDetail(@ModelAttribute ProductDetail productDetail) {
		logger.info("setProductDetail " + productDetail);
		Map<String, Object> result = new HashMap<String, Object>();

		if(productDetail.getProduct_detail_id() == 0) { 
			sellerService.addProductDetail(productDetail);
		} else {
			sellerService.updateProductDetail(productDetail);
		}

		result.put("msg", "success");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	// 판매자 쿠폰 조회
	@GetMapping("/coupon")
	public ResponseEntity<Map<String, Object>> getSellerCouponList(@RequestParam("seller_id") String seller_id) {
		logger.info("getSellerCoupon "+ seller_id);
		Map<String, Object> result = new HashMap<String, Object>();
		
		List<Coupon> couponList = sellerService.getSellerCouponList(seller_id);
		
		result.put("couponList", couponList);
		result.put("msg", "success");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	// 쿠폰 등록
	@PostMapping("/coupon")
	public ResponseEntity<Map<String, Object>> addCoupon(@ModelAttribute Coupon coupon) {
		logger.info("addCoupon "+ coupon);
		Map<String, Object> result = new HashMap<String, Object>();
		
		sellerService.addCoupon(coupon);
		
		result.put("msg", "success");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	// 쿠폰 상태변경
	@PostMapping("/coupon/status")
	public ResponseEntity<Map<String, Object>> disabledCoupon(@ModelAttribute Coupon coupon) {
		logger.info("disabledCoupon "+ coupon.getCoupon_id());
		Map<String, Object> result = new HashMap<String, Object>();
		
		sellerService.updateCouponStatus(coupon);
		
		result.put("msg", "success");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	// 판매자 해당 쿠폰 허용제품 조회
	@GetMapping("/coupon/allowed")
	public ResponseEntity<Map<String, Object>> getSellerCouponAllow(@RequestParam("coupon_id") String coupon_id) {
		logger.info("getSellerCouponAllow "+ coupon_id);
		Map<String, Object> result = new HashMap<String, Object>();
		
		List<SellerCouponAllowedProductDTO> CouponAllowedProductList = sellerService.getSellerCouponAllow(coupon_id);
		
		result.put("CouponAllowedProductList", CouponAllowedProductList);
		result.put("msg", "success");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	// 판매자 해당 쿠폰 허용제품 변경
	@PostMapping("/coupon/allowed")
	public ResponseEntity<Map<String, Object>> setSellerCouponAllow(@RequestParam("coupon_id") String coupon_id, @RequestParam("productIds") List<Integer> productIds) {
		logger.info("getSellerCouponAllow "+ coupon_id + " => " + productIds);
		Map<String, Object> result = new HashMap<String, Object>();
		
		sellerService.setSellerCouponAllow(coupon_id, productIds);
		
		result.put("msg", "success");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	// 해당 쿠폰을 유저에게 발행하기
	@PostMapping("/coupon/usercoupon")
	public ResponseEntity<Map<String, Object>> issueCouponsToUsers(@RequestParam("coupon_id") String coupon_id, @RequestParam("userIds") List<String> userIds) {
		logger.info("issueCouponsToUsers "+ coupon_id + " : " + userIds);
		Map<String, Object> result = new HashMap<String, Object>();
		
		sellerService.issueCouponsToUsers(coupon_id, userIds);
		
		result.put("msg", "success");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	// 판매자와 관련된 회원 조회(내 상품을 보거나 위시하거나 장바구니에 넣거나 즐겨찾기한) 
	@GetMapping("/user/interesting")
	public ResponseEntity<Map<String, Object>> getSellerInterestingUser(@RequestParam("seller_id") String seller_id) {
		logger.info("getSellerInterestingUser "+ seller_id);
		Map<String, Object> result = new HashMap<String, Object>();
		
		List<ProductViewCountDTO> productViewCountList = sellerService.getProductViewCountList(seller_id);
		
		List<ProductWishCountDTO> productWishCountList = sellerService.getProductWishCountList(seller_id);
		
		List<userInBookmarkDTO> brandBookmarkList = sellerService.getBrandBookmarkList(seller_id);
		
		List<UserInCartCountDTO> userInCartCountList = sellerService.getUserInCartCountList(seller_id);
		
		// 주문액수, 주문 갯수 가져오기
		
		result.put("productViewCountList", productViewCountList);
		result.put("productWishCountList", productWishCountList);
		result.put("brandBookmarkList", brandBookmarkList);
		result.put("userInCartCountList", userInCartCountList);
		result.put("msg", "success");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	// 유저쿠폰사용내역 조회
	@GetMapping("/coupon/usercoupon")
	public ResponseEntity<Map<String, Object>> getSellerUsercouponUsed(@RequestParam("seller_id") String seller_id) {
		logger.info("getSellerUsercouponUsed "+ seller_id);
		Map<String, Object> result = new HashMap<String, Object>();
		
		result.put("msg", "success");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	// 유저쿠폰사용내역 상세조회 - 어느주문에 사용했는지
	
		
}
