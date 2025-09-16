package me._hanho.nextjs_shop.mypage;

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
import me._hanho.nextjs_shop.model.Coupon;

@RestController
@RequestMapping("/bapi/mypage")
public class MypageController {

	private static final Logger logger = LoggerFactory.getLogger(MypageController.class);
	
	@Autowired
	private MypageService mypageService;
	
	// 유저 쿠폰 조회
	@GetMapping("/usercoupon")
	public ResponseEntity<Map<String, Object>> getUserCoupons(@RequestParam("user_id") String user_id) {
		logger.info("getUserCoupons : " + user_id);
		Map<String, Object> result = new HashMap<String, Object>();
		
		List<UserCouponDTO> coupon_list = mypageService.getUserCoupons(user_id);
		
		result.put("coupon_list", coupon_list);
		result.put("msg", "success");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	// 주문배송정보 조회
	@GetMapping("/my-order")
	public ResponseEntity<Map<String, Object>> getMyReviews(@RequestParam("user_id") String user_id) {
		logger.info("getMyReviews : user_id");
		Map<String, Object> result = new HashMap<String, Object>();
		
		List<MyOrderWithReviewDTO> myOrderWithReview = mypageService.getMyOrderWithReview(user_id);

		result.put("msg", "success");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	// 작성가능한 리뷰와 작성했던 리뷰
	@GetMapping("/review")
	public ResponseEntity<Map<String, Object>> getReviews(@RequestParam("user_id") String user_id) {
		logger.info("getMyReviews : " + user_id);
		Map<String, Object> result = new HashMap<String, Object>();
		
		List<MyOrderWithReviewDTO> myOrderWithReview = mypageService.getMyOrderWithReview(user_id);
		
		result.put("msg", "success");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	// 리뷰 작성
	@PostMapping("/review")
	public ResponseEntity<Map<String, Object>> writeReview() {
		logger.info("writeReview : ");
		Map<String, Object> result = new HashMap<String, Object>();
		
		result.put("msg", "success");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	// 장바구니 조회
	@GetMapping("/cart")
	public ResponseEntity<Map<String, Object>> selectCart(@RequestParam("user_id") String user_id) {
		logger.info("selectCart : " + user_id);
		Map<String, Object> result = new HashMap<String, Object>();
		
		List<CartProductDTO> CartList = mypageService.getCartList(user_id);
		
		result.put("CartList", CartList);
		result.put("msg", "success");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	// 장바구니 제품 수량/선택여부 변경
	@PostMapping("/cart")
	public ResponseEntity<Map<String, Object>> updateCart(@ModelAttribute Cart cart) {
		logger.info("updateCart : " + cart.getCart_id());
		Map<String, Object> result = new HashMap<String, Object>();

		mypageService.updateCart(cart);
		
		result.put("msg", "success");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	// 장바구니 제품 삭제
	@DeleteMapping("/cart/{cart_id}")
	public ResponseEntity<Map<String, Object>> deleteCart(@PathVariable("cart_id") int cart_id) {
		logger.info("deleteCart : " + cart_id);
		Map<String, Object> result = new HashMap<String, Object>();
		
		mypageService.deleteCart(cart_id);
		
		result.put("msg", "success");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	// 위시리스트 조회
	@GetMapping("/wish")
	public ResponseEntity<Map<String, Object>> getWishList(@RequestParam("user_id") String user_id) {
		logger.info("getWishList : " + user_id);
		Map<String, Object> result = new HashMap<String, Object>();
		
		List<WishlistItemDTO> wishlistItems = mypageService.getWishlistItems(user_id);
		
		result.put("wishlistItems", wishlistItems);
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	// 위시리스트 삭제
	@PostMapping("/wish")
	public ResponseEntity<Map<String, Object>> addWish() {
		logger.info("addWish : ");
		Map<String, Object> result = new HashMap<String, Object>();
		
		result.put("msg", "success");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	// 위시리스트 삭제
	@DeleteMapping("/wish/{wish_id}")
	public ResponseEntity<Map<String, Object>> deleteWish(@PathVariable("wish_id") int wish_id) {
		logger.info("deleteWish : " + wish_id);
		Map<String, Object> result = new HashMap<String, Object>();
		
		mypageService.deleteWish(wish_id);
		
		result.put("msg", "success");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	
	
}
