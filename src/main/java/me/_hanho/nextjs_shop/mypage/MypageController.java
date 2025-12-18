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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import me._hanho.nextjs_shop.model.Cart;
import me._hanho.nextjs_shop.model.ProductDetail;
import me._hanho.nextjs_shop.model.Review;
import me._hanho.nextjs_shop.model.UserAddress;

@RestController
@RequestMapping("/bapi/mypage")
public class MypageController {

	private static final Logger logger = LoggerFactory.getLogger(MypageController.class);
	
	@Autowired
	private MypageService mypageService;
	
	// 유저 쿠폰 조회
	@GetMapping("/user-coupon")
	public ResponseEntity<Map<String, Object>> getUserCoupons(@RequestParam("userId") String userId) {
		logger.info("getUserCoupons : " + userId);
		Map<String, Object> result = new HashMap<String, Object>();
		
		List<UserCouponDTO> couponList = mypageService.getUserCoupons(userId);
		
		result.put("couponList", couponList);
		result.put("message", "USER_COUPON_FETCH_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	// 주문배송정보 조회
	@GetMapping("/my-order")
	public ResponseEntity<Map<String, Object>> getMyOrderList(@RequestParam("userId") String userId) {
		logger.info("getMyReviews : " + userId);
		Map<String, Object> result = new HashMap<String, Object>();
		
		List<MyOrderGroupDTO> myOrderList = mypageService.getMyOrderListWithReview(userId);

		result.put("myOrderList", myOrderList);
		result.put("message", "MY_ORDER_LIST_FETCH_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	// 주문배송정보 상세조회
	@GetMapping("/my-order/{orderId}")
	public ResponseEntity<Map<String, Object>> getMyOrderDetail(@PathVariable("orderId") String orderId) {
		logger.info("getMyReviews : " + orderId);
		Map<String, Object> result = new HashMap<String, Object>();
		
		MyOrderDetailDTO myOrderDetail = mypageService.getMyOrderDetail(orderId);

		result.put("myOrderDetail", myOrderDetail);
		result.put("message", "MY_ORDER_DETAIL_FETCH_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	// 리뷰 작성
	@PostMapping("/review")
	public ResponseEntity<Map<String, Object>> writeReview(@ModelAttribute Review review) {
		logger.info("writeReview : ");
		Map<String, Object> result = new HashMap<String, Object>();
		
		mypageService.insertReview(review);
		
		result.put("message", "REVIEW_WRITE_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	// 장바구니 조회
	@GetMapping("/cart")
	public ResponseEntity<Map<String, Object>> selectCart(@RequestParam("userId") String userId) {
		logger.info("selectCart : " + userId);
		Map<String, Object> result = new HashMap<String, Object>();
		
		List<CartProductDTO> CartList = mypageService.getCartList(userId);
		
		result.put("cartList", CartList);
		result.put("message", "CART_FETCH_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	// 장바구니 제품 옵션/수량 변경
	@PostMapping("/cart")
	public ResponseEntity<Map<String, Object>> updateCart(@ModelAttribute Cart cart) {
		logger.info("updateCart : " + cart.getCartId());
		Map<String, Object> result = new HashMap<String, Object>();

		mypageService.updateCart(cart);
		
		result.put("message", "CART_UPDATE_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	// 장바구니 제품 선택여부 변경
	@PutMapping("/cart")
	public ResponseEntity<Map<String, Object>> updateSelectedCart(@ModelAttribute UpdateSelectedCartDTO selectedCart, 
			@RequestAttribute("userId") String userId) {
		logger.info("updateSelectedCart : " + selectedCart);
		Map<String, Object> result = new HashMap<String, Object>();

		mypageService.updateSelectedCart(selectedCart);
		
		result.put("message", "CART_SELECTED_UPDATE_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	// 장바구니 제품 삭제
	@DeleteMapping("/cart")
	public ResponseEntity<Map<String, Object>> deleteCart(@RequestParam("cartIdList") List<Integer> cartIdList) {
		logger.info("deleteCart : " + cartIdList);
		Map<String, Object> result = new HashMap<String, Object>();
		
		mypageService.deleteCart(cartIdList);
		
		result.put("message", "CART_DELETE_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	// 장바구니 옵션변경 - 장바구니 제품 다른 detail조회
	@GetMapping("/cart/option/product-detail")
	public ResponseEntity<Map<String, Object>> getCartOptionProductDetailList(@RequestParam("productId") int productId) {
		logger.info("getCartOptionProductDetail : " + productId);
		Map<String, Object> result = new HashMap<String, Object>();
		
		List<ProductDetail> cartOptionProductDetailList = mypageService.getCartOptionProductDetailList(productId);
		
		result.put("cartOptionProductDetailList", cartOptionProductDetailList);
		result.put("message", "CART_OPTION_FETCH_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	// 위시리스트 조회
	@GetMapping("/wish")
	public ResponseEntity<Map<String, Object>> getWishList(@RequestParam("userId") String userId) {
		logger.info("getWishList : " + userId);
		Map<String, Object> result = new HashMap<String, Object>();
		
		List<WishlistItemDTO> wishlistItems = mypageService.getWishlistItems(userId);
		
		result.put("wishlistItems", wishlistItems);
		result.put("message", "WISH_FETCH_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	// 위시리스트 추가(실행 취소) => ProductController(/bapi/product/wish)에 있음.
	
	// 유저배송지 조회
	@GetMapping("/address")
	public ResponseEntity<Map<String, Object>> getUserAddressList(@RequestParam("userId") String userId) {
		logger.info("getUserAddress : " + userId);
		Map<String, Object> result = new HashMap<String, Object>();
		
		List<UserAddress> userAddressList = mypageService.getUserAddressList(userId);
		
		result.put("userAddressList", userAddressList);
		result.put("message", "ADDRESS_FETCH_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	// 유저배송지 추가/수정
	@PostMapping("/address")
	public ResponseEntity<Map<String, Object>> setUserAddress(@ModelAttribute UserAddress userAddress) {
		logger.info("setUserAddress : " + userAddress);
		Map<String, Object> result = new HashMap<String, Object>();
		
		if(userAddress.getAddressId() == 0) {
			mypageService.insertUserAddress(userAddress);
		} else {
			mypageService.updateUserAddress(userAddress);
		}
		
		result.put("message", "ADDRESS_SAVE_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	// 유저배송지 삭제
	@DeleteMapping("/address/{addressId}")
	public ResponseEntity<Map<String, Object>> deleteUserAddress(@PathVariable("addressId") int addressId) {
		logger.info("deleteUserAddress : " + addressId);
		Map<String, Object> result = new HashMap<String, Object>();
		
		mypageService.deleteUserAddress(addressId);
		
		result.put("message", "ADDRESS_DELETE_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
}
