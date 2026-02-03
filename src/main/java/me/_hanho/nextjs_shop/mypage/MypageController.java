package me._hanho.nextjs_shop.mypage;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me._hanho.nextjs_shop.common.exception.BusinessException;
import me._hanho.nextjs_shop.common.exception.ErrorCode;
import me._hanho.nextjs_shop.product.ProductOptionResponse;
import me._hanho.nextjs_shop.product.ProductService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bapi/mypage")
public class MypageController {

	private static final Logger logger = LoggerFactory.getLogger(MypageController.class);
	
	private final MypageService mypageService;
	
	private final ProductService productService;
	
	// 유저 쿠폰 조회
	@GetMapping("/user-coupon")
	public ResponseEntity<Map<String, Object>> getUserCoupons(
			@RequestAttribute(value="userNo", required=false) Integer userNo) {
		if (userNo == null) throw new BusinessException(ErrorCode.AUTHENTICATION_REQUIRED);
		logger.info("getUserCoupons : " + userNo);
		Map<String, Object> result = new HashMap<String, Object>();
		
		List<UserCouponResponse> couponList = mypageService.getUserCoupons(userNo);
		
		result.put("couponList", couponList);
		result.put("message", "USER_COUPON_FETCH_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	// 주문배송정보 조회
	@GetMapping("/my-order")
	public ResponseEntity<Map<String, Object>> getMyOrderList(
			@RequestAttribute(value="userNo", required=false) Integer userNo) {
		if (userNo == null) throw new BusinessException(ErrorCode.AUTHENTICATION_REQUIRED);
		logger.info("getMyOrderList : " + userNo);
		Map<String, Object> result = new HashMap<String, Object>();
		
		List<MyOrderGroupResponse> myOrderList = mypageService.getMyOrderListWithReview(userNo);

		result.put("myOrderList", myOrderList);
		result.put("message", "MY_ORDER_LIST_FETCH_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	// 주문배송정보 상세조회
	@GetMapping("/my-order/{orderId}")
	public ResponseEntity<Map<String, Object>> getMyOrderDetail(
			@PathVariable("orderId") String orderId,
			@RequestAttribute(value="userNo", required=false) Integer userNo) {
		if (userNo == null) throw new BusinessException(ErrorCode.AUTHENTICATION_REQUIRED);
		logger.info("getMyReviews : " + orderId);
		Map<String, Object> result = new HashMap<String, Object>();
		
		MyOrderDetailResponse myOrderDetail = mypageService.getMyOrderDetail(orderId, userNo);

		result.put("myOrderDetail", myOrderDetail);
		result.put("message", "MY_ORDER_DETAIL_FETCH_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	// 리뷰 작성
	@PostMapping("/review")
	public ResponseEntity<Map<String, Object>> addReview(
			@Valid @ModelAttribute AddReviewRequest review, 
			@RequestAttribute(value="userNo", required=false) Integer userNo) {
		if (userNo == null) throw new BusinessException(ErrorCode.AUTHENTICATION_REQUIRED);
		logger.info("addReview review {} : " + review + ", userNo : " + userNo);
		Map<String, Object> result = new HashMap<String, Object>();
		
		mypageService.insertReview(review, userNo);
		
		result.put("message", "REVIEW_INSERT_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	// 장바구니 조회
	@GetMapping("/cart")
	public ResponseEntity<Map<String, Object>> selectCart(
			@RequestAttribute(value="userNo", required=false) Integer userNo) {
		if (userNo == null) throw new BusinessException(ErrorCode.AUTHENTICATION_REQUIRED);
		logger.info("selectCart : " + userNo);
		Map<String, Object> result = new HashMap<String, Object>();
		
		List<CartProductResponse> CartList = mypageService.getCartList(userNo);
		
		result.put("cartList", CartList);
		result.put("message", "CART_FETCH_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	// 장바구니 제품 옵션/수량 변경
	@PostMapping("/cart")
	public ResponseEntity<Map<String, Object>> updateCart(
			@Valid @ModelAttribute UpdateCartRequest cart, 
			@RequestAttribute(value="userNo", required=false) Integer userNo) {
		if (userNo == null) throw new BusinessException(ErrorCode.AUTHENTICATION_REQUIRED);
		logger.info("updateCart : " + cart.getCartId());
		Map<String, Object> result = new HashMap<String, Object>();

		mypageService.updateCart(cart, userNo);
		
		result.put("message", "CART_UPDATE_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	// 장바구니 제품 선택여부 변경
	@PutMapping("/cart")
	public ResponseEntity<Map<String, Object>> updateSelectedCart(
			@RequestParam("cartIdList") List<Integer> cartIdList,
			@RequestParam("selected") Boolean selected,
			@RequestAttribute(value="userNo", required=false) Integer userNo) {
		if (userNo == null) throw new BusinessException(ErrorCode.AUTHENTICATION_REQUIRED);
		logger.info("updateSelectedCart cartIdList {} : " + cartIdList + ", selected : " + selected);
		Map<String, Object> result = new HashMap<String, Object>();

		mypageService.updateSelectedCart(cartIdList, selected, userNo);
		
		result.put("message", "CART_SELECTED_UPDATE_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	// 장바구니 제품 삭제
	@DeleteMapping("/cart")
	public ResponseEntity<Map<String, Object>> deleteCart(
			@RequestParam("cartIdList") List<Integer> cartIdList,
			@RequestAttribute(value="userNo", required=false) Integer userNo) {
		if (userNo == null) throw new BusinessException(ErrorCode.AUTHENTICATION_REQUIRED);
		logger.info("deleteCart : " + cartIdList);
		Map<String, Object> result = new HashMap<String, Object>();
		
		mypageService.deleteCart(cartIdList, userNo);
		
		result.put("message", "CART_DELETE_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	// 장바구니 옵션조회 - 장바구니 제품 다른 option조회
	@GetMapping("/cart/option")
	public ResponseEntity<Map<String, Object>> getCartOptionProductOptionList(
			@RequestParam("productId") Integer productId) {
		logger.info("getCartOptionProductOptionList : " + productId);
		Map<String, Object> result = new HashMap<String, Object>();
		
		List<ProductOptionResponse> cartOptionProductOptionList = productService.getProductOptionList(productId);
		
		result.put("cartOptionProductOptionList", cartOptionProductOptionList);
		result.put("message", "CART_OPTION_FETCH_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	// 위시리스트 조회
	@GetMapping("/wish")
	public ResponseEntity<Map<String, Object>> getWishList(
			@RequestAttribute(value="userNo", required=false) Integer userNo) {
		if (userNo == null) throw new BusinessException(ErrorCode.AUTHENTICATION_REQUIRED);
		logger.info("getWishList : " + userNo);
		Map<String, Object> result = new HashMap<String, Object>();
		
		List<WishlistItemResponse> wishlistItems = mypageService.getWishlistItems(userNo);
		
		result.put("wishlistItems", wishlistItems);
		result.put("message", "WISH_FETCH_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	// 위시리스트 추가(실행 취소) => ProductController(/bapi/product/wish)에 있음.
	
	// 유저배송지 조회
	@GetMapping("/address")
	public ResponseEntity<Map<String, Object>> getUserAddressList(
			@RequestAttribute(value="userNo", required=false) Integer userNo) {
		if (userNo == null) throw new BusinessException(ErrorCode.AUTHENTICATION_REQUIRED);
		logger.info("getUserAddress : " + userNo);
		Map<String, Object> result = new HashMap<String, Object>();
		
		List<UserAddressResponse> userAddressList = mypageService.getUserAddressList(userNo);
		
		result.put("userAddressList", userAddressList);
		result.put("message", "ADDRESS_FETCH_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	// 유저배송지 추가
	@PostMapping("/address")
	public ResponseEntity<Map<String, Object>> addUserAddress(
			@Valid @ModelAttribute AddUserAddressRequest userAddress,
			@RequestAttribute(value="userNo", required=false) Integer userNo) {
		if (userNo == null) throw new BusinessException(ErrorCode.AUTHENTICATION_REQUIRED);
		logger.info("addUserAddress : " + userAddress);
		Map<String, Object> result = new HashMap<String, Object>();
		
		mypageService.insertUserAddress(userAddress, userNo);
		
		result.put("message", "ADDRESS_ADD_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	// 유저배송지 수정
	@PutMapping("/address")
	public ResponseEntity<Map<String, Object>> updateUserAddress(
			@Valid @ModelAttribute UpdateUserAddressRequest userAddress,
			@RequestAttribute(value="userNo", required=false) Integer userNo) {
		if (userNo == null) throw new BusinessException(ErrorCode.AUTHENTICATION_REQUIRED);
		logger.info("updateUserAddress : " + userAddress);
		Map<String, Object> result = new HashMap<String, Object>();
		
		mypageService.updateUserAddress(userAddress, userNo);
		
		result.put("message", "ADDRESS_UPDATE_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	// 유저배송지 삭제
	@DeleteMapping("/address/{addressId}")
	public ResponseEntity<Map<String, Object>> deleteUserAddress(
			@PathVariable("addressId") Integer addressId,
			@RequestAttribute(value="userNo", required=false) Integer userNo) {
		if (userNo == null) throw new BusinessException(ErrorCode.AUTHENTICATION_REQUIRED);
		logger.info("deleteUserAddress : " + addressId);
		Map<String, Object> result = new HashMap<String, Object>();
		
		mypageService.deleteUserAddress(addressId, userNo);
		
		result.put("message", "ADDRESS_DELETE_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
}
