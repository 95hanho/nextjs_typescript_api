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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me._hanho.nextjs_shop.common.exception.BusinessException;
import me._hanho.nextjs_shop.common.exception.ErrorCode;
import me._hanho.nextjs_shop.mypage.dto.AddReviewRequest;
import me._hanho.nextjs_shop.mypage.dto.AddUserAddressRequest;
import me._hanho.nextjs_shop.mypage.dto.AvailableCartCouponAtCartResponse;
import me._hanho.nextjs_shop.mypage.dto.AvailableSellerCouponAtCartResponse;
import me._hanho.nextjs_shop.mypage.dto.CartSummaryResponse;
import me._hanho.nextjs_shop.mypage.dto.MyOrderDetailItem;
import me._hanho.nextjs_shop.mypage.dto.MyOrderDetailResponse;
import me._hanho.nextjs_shop.mypage.dto.MyOrderGroupResponse;
import me._hanho.nextjs_shop.mypage.dto.ReviewOrderInfoResponse;
import me._hanho.nextjs_shop.mypage.dto.ReviewResponse;
import me._hanho.nextjs_shop.mypage.dto.SetReviewImageRequest;
import me._hanho.nextjs_shop.mypage.dto.UpdateCartRequest;
import me._hanho.nextjs_shop.mypage.dto.UpdateReviewRequest;
import me._hanho.nextjs_shop.mypage.dto.UpdateUserAddressRequest;
import me._hanho.nextjs_shop.mypage.dto.UserAddressResponse;
import me._hanho.nextjs_shop.mypage.dto.UserCouponResponse;
import me._hanho.nextjs_shop.mypage.dto.WishlistItemResponse;
import me._hanho.nextjs_shop.product.ProductService;
import me._hanho.nextjs_shop.product.dto.ProductOptionResponse;
import me._hanho.nextjs_shop.seller.dto.SetProductImageRequest;

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
		logger.info("[getUserCoupons] userNo={}", userNo);
		Map<String, Object> result = new HashMap<String, Object>();
		
		List<UserCouponResponse> couponList = mypageService.getUserCoupons(userNo);
		
		result.put("couponList", couponList);
		result.put("message", "USER_COUPON_FETCH_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	// 주문배송정보 조회
	@GetMapping("/my-order")
	public ResponseEntity<Map<String, Object>> getMyOrderList(
			@RequestAttribute(value="userNo", required=false) Integer userNo,
			@RequestParam(value="keyword", required=false) String keyword) {
		if (userNo == null) throw new BusinessException(ErrorCode.AUTHENTICATION_REQUIRED);
		logger.info("[getMyOrderList] userNo={}", userNo);
		Map<String, Object> result = new HashMap<String, Object>();
		
		List<MyOrderGroupResponse> myOrderList = mypageService.getMyOrderList(userNo, keyword);

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
		logger.info("[getMyOrderDetail] orderId={}, userNo={}", orderId, userNo);
		Map<String, Object> result = new HashMap<String, Object>();
		
		MyOrderDetailResponse myOrderDetail = mypageService.getMyOrderDetail(orderId, userNo);
		List<MyOrderDetailItem> items = mypageService.getMyOrderDetailItems(orderId, userNo);
		
		if(myOrderDetail == null || items == null || items.isEmpty()) {
			throw new BusinessException(ErrorCode.MY_ORDER_DETAIL_NOT_FOUND);
		}

		result.put("myOrderDetail", myOrderDetail);
		result.put("myOrderDetailItems", items);
		result.put("message", "MY_ORDER_DETAIL_FETCH_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	// 리뷰 및 주문정보 조회
	@GetMapping("/review")
	public ResponseEntity<Map<String, Object>> getReviewOrderInfo(
			@RequestParam("orderItemId") Integer orderItemId,
			@RequestAttribute(value="userNo", required=false) Integer userNo) {
		if (userNo == null) throw new BusinessException(ErrorCode.AUTHENTICATION_REQUIRED);
		logger.info("[getReviewOrderInfo] orderItemId={}, userNo={}", orderItemId, userNo);
		Map<String, Object> result = new HashMap<String, Object>();
		
		ReviewOrderInfoResponse reviewOrderItem = mypageService.getReviewOrderInfo(orderItemId, userNo);
		ReviewResponse review = mypageService.getReviewByOrderItemId(orderItemId, userNo);
		
		result.put("reviewOrderItem", reviewOrderItem);
		result.put("review", review);
		result.put("message", "REVIEW_ORDER_INFO_FETCH_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	// 리뷰 작성
	@PostMapping("/review")
	public ResponseEntity<Map<String, Object>> addReview(
			@Valid @ModelAttribute AddReviewRequest review, 
			@RequestAttribute(value="userNo", required=false) Integer userNo) {
		if (userNo == null) throw new BusinessException(ErrorCode.AUTHENTICATION_REQUIRED);
		logger.info("[addReview] review={}, userNo={}", review, userNo);
		Map<String, Object> result = new HashMap<String, Object>();
		
		int reviewId = mypageService.insertReview(review, userNo);
		
		result.put("reviewId", reviewId);
		result.put("message", "REVIEW_INSERT_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	// 리뷰 수정
	@PutMapping("/review")
	public ResponseEntity<Map<String, Object>> updateReview(
			@Valid @ModelAttribute UpdateReviewRequest review, 
			@RequestAttribute(value="userNo", required=false) Integer userNo) {
		if (userNo == null) throw new BusinessException(ErrorCode.AUTHENTICATION_REQUIRED);
		logger.info("[updateReview] review={}, userNo={}", review, userNo);
		Map<String, Object> result = new HashMap<String, Object>();
		
		mypageService.updateReview(review, userNo);
		
		result.put("message", "REVIEW_UPDATE_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	// 리뷰 이미지 수정
	@PostMapping("/review/image")
	public ResponseEntity<Map<String, Object>> setReviewImage(
		@RequestPart("request") SetReviewImageRequest reviewImageRequest,
		@RequestPart(value = "files", required = false) List<MultipartFile> files,
		@RequestAttribute(value="userNo", required=false) Integer userNo) {
		if (userNo == null) throw new BusinessException(ErrorCode.AUTHENTICATION_REQUIRED);
		logger.info("[setReviewImage] request={}, userNo={}", reviewImageRequest, userNo);
		logger.info("[setReviewImage] files size={}", files != null ? files.size() : 0);
		Map<String, Object> result = new HashMap<String, Object>();
		
		mypageService.setReviewImages(reviewImageRequest, files, userNo);
		
		result.put("message", "REVIEW_IMAGE_UPDATE_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	// 장바구니 조회
	@GetMapping("/cart")
	public ResponseEntity<Map<String, Object>> selectCart(
			@RequestAttribute(value="userNo", required=false) Integer userNo) {
		if (userNo == null) throw new BusinessException(ErrorCode.AUTHENTICATION_REQUIRED);
		logger.info("[selectCart] userNo={}", userNo);
		Map<String, Object> result = new HashMap<String, Object>();
		
		// 장바구니 제품리스트, 장바구니 제품 Ids, 재고 부족 여부 조회
		CartSummaryResponse cartSummary = mypageService.getCartSummary(userNo);

		// 장바구니에 담긴 제품들에 대한 이용가능 장바구니 쿠폰 조회
		List<AvailableCartCouponAtCartResponse> availableCartCoupons = mypageService.getAvailableCartCouponsAtCart(cartSummary.getProductIds(), userNo);
		// 장바구니에 담긴 제품들에 대한 이용가능 판매자 쿠폰 조회 
		List<AvailableSellerCouponAtCartResponse> availableSellerCoupons = mypageService.getAvailableSellerCouponsAtCart(cartSummary.getProductIds(), userNo);

		result.put("isExceedQuantity", cartSummary.isExceedQuantity());
		result.put("cartList", cartSummary.getCartList());
		result.put("availableCartCoupons", availableCartCoupons);
		result.put("availableSellerCoupons", availableSellerCoupons);
		result.put("message", "CART_FETCH_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	// 장바구니 제품 옵션/수량 변경
	@PostMapping("/cart")
	public ResponseEntity<Map<String, Object>> updateCart(
			@Valid @ModelAttribute UpdateCartRequest cart, 
			@RequestAttribute(value="userNo", required=false) Integer userNo) {
		if (userNo == null) throw new BusinessException(ErrorCode.AUTHENTICATION_REQUIRED);
		logger.info("[updateCart] cartId={}, userNo={}", cart.getCartId(), userNo);
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
		logger.info("[updateSelectedCart] cartIdList={}, selected={}, userNo={}", cartIdList, selected, userNo);
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
		logger.info("[deleteCart] cartIdList={}, userNo={}", cartIdList, userNo);
		Map<String, Object> result = new HashMap<String, Object>();
		
		mypageService.deleteCart(cartIdList, userNo);
		
		result.put("message", "CART_DELETE_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	// 장바구니 옵션조회 - 장바구니 제품 다른 option조회
	@GetMapping("/cart/option")
	public ResponseEntity<Map<String, Object>> getCartOptionProductOptionList(
			@RequestParam("productId") Integer productId) {
		logger.info("[getCartOptionProductOptionList] productId={}", productId);
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
		logger.info("[getWishList] userNo={}", userNo);
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
		logger.info("[getUserAddressList] userNo={}", userNo);
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
		logger.info("[addUserAddress] userAddress={}, userNo={}", userAddress, userNo);
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
		logger.info("[updateUserAddress] userAddress={}, userNo={}", userAddress, userNo);
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
		logger.info("[deleteUserAddress] addressId={}, userNo={}", addressId, userNo);
		Map<String, Object> result = new HashMap<String, Object>();
		
		mypageService.deleteUserAddress(addressId, userNo);
		
		result.put("message", "ADDRESS_DELETE_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
}
