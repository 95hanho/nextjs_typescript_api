package me._hanho.nextjs_shop.seller;

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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me._hanho.nextjs_shop.auth.AuthService;
import me._hanho.nextjs_shop.auth.TokenDTO;
import me._hanho.nextjs_shop.auth.TokenService;
import me._hanho.nextjs_shop.common.exception.BusinessException;
import me._hanho.nextjs_shop.common.exception.ErrorCode;
import me._hanho.nextjs_shop.model.Coupon;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bapi/seller")
public class SellerController {
	
	private static final Logger logger = LoggerFactory.getLogger(SellerController.class);
	
	private final SellerService sellerService;
	private final AuthService authService;
	private final TokenService tokenService;
	
	// 로그인
	@PostMapping
	public ResponseEntity<Map<String, Object>> login(@RequestParam("sellerId") String sellerId, @RequestParam("password") String password) {
		logger.info("sellerLogin :" + sellerId);
		Map<String, Object> result = new HashMap<String, Object>();
		
		SellerLoginDTO checkSeller = sellerService.isSeller(sellerId);
		System.out.println("checkSeller : " + checkSeller);
		if (checkSeller == null || !sellerService.passwordCheck(password, checkSeller.getPassword())) {
			result.put("message", "SELLER_NOT_FOUND"); // 입력하신 아이디 또는 비밀번호가 일치하지 않습니다
			logger.error("입력하신 아이디 또는 비밀번호가 일치하지 않습니다");
			
			return new ResponseEntity<>(
					result
					, HttpStatus.UNAUTHORIZED);
		} else {
			result.put("sellerNo", checkSeller.getSellerNo());
			result.put("message", "LOGIN_SUCCESS");
			return new ResponseEntity<>(
					result
					, HttpStatus.OK);
		}
	}
	// 로그인 토큰 저장
	@PostMapping("/token")
	public ResponseEntity<Map<String, Object>> tokenStore(
	        @RequestAttribute("sellerNo") Integer sellerNo,
	        @RequestParam("refreshToken") String refreshToken,
	        @RequestHeader("user-agent") String userAgent,
	        @RequestHeader("x-forwarded-for") String forwardedFor
	) {
	    tokenService.parseJwtRefreshToken(refreshToken); // 여기서 유효하지 않으면 예외 던지게
	
	    String ipAddress = forwardedFor != null ? forwardedFor : "unknown";
	    SellerToken token = SellerToken.builder()
	            .connectIp(ipAddress)
	            .connectAgent(userAgent)
	            .refreshToken(refreshToken)
	            .build();
	
	    sellerService.insertToken(token, sellerNo);
	
	    return ResponseEntity.ok(Map.of("message", "SELLER_TOKEN_INSERT_SUCCESS"));
	}
	// 로그인 토큰 수정(재저장)
	@PostMapping("/token/refresh")
	public ResponseEntity<Map<String, Object>> updateToken(
	        @RequestParam("beforeToken") String beforeToken,
	        @RequestParam("refreshToken") String refreshToken,
	        @RequestHeader("user-agent") String userAgent,
	        @RequestHeader("x-forwarded-for") String forwardedFor
	) {
	    tokenService.parseJwtRefreshToken(beforeToken);
	    tokenService.parseJwtRefreshToken(refreshToken);

	    String ipAddress = forwardedFor != null ? forwardedFor : "unknown";
	    TokenDTO token = TokenDTO.builder()
	            .connectIp(ipAddress)
	            .connectAgent(userAgent)
	            .refreshToken(refreshToken)
	            .beforeToken(beforeToken)
	            .build();

	    authService.updateToken(token);

	    Integer sellerNo = sellerService.getSellerNoByToken(token);
	    if (sellerNo == null) {
	        throw new BusinessException(ErrorCode.WRONG_TOKEN);
	    }

	    return ResponseEntity.ok(Map.of(
	            "sellerNo", sellerNo,
	            "message", "TOKEN_UPDATE_SUCCESS"
	    ));
	}
	// 판매자 정보조회
	@GetMapping
	public ResponseEntity<Map<String, Object>> getSeller(@RequestAttribute("sellerNo") Integer sellerNo) {
		logger.info("getSeller :" + sellerNo);
		Map<String, Object> result = new HashMap<String, Object>();
		
		SellerInfoResponse sellerInfo = sellerService.getSeller(sellerNo);
		
		result.put("seller", sellerInfo);
		result.put("message", "SELLER_FETCH_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	// 판매자id 중복확인
	@PostMapping("/id")
	public ResponseEntity<Map<String, Object>> sellerIdDuplCheck(@RequestParam("sellerId") String sellerId) {
		logger.info("sellerIdDuplCheck sellerId=" + sellerId);
		Map<String, Object> result = new HashMap<String, Object>();
		
		boolean hasId = sellerService.hasId(sellerId);
		logger.info("hasId : " + hasId);
		
		if(hasId) {
			result.put("message", "SELLER_ID_DUPLICATED");
			return new ResponseEntity<>(result, HttpStatus.CONFLICT);
		} else {
			result.put("message", "SELLER_ID_AVAILABLE");
			return new ResponseEntity<>(result, HttpStatus.OK);
		}
	}
	// 판매자 등록요청(회원가입)
	@PostMapping("/registration")
	public ResponseEntity<Map<String, Object>> sellerRegister(@Valid @ModelAttribute SellerRegisterRequest seller) {
		logger.info("sellerRegister");
		Map<String, Object> result = new HashMap<String, Object>();
		
		sellerService.setSeller(seller);
		
		result.put("message", "SELLER_REGISTER_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	// 제품 조회
	@GetMapping("/product")
	public ResponseEntity<Map<String, Object>> getSellerProductList(@RequestAttribute("sellerNo") Integer sellerNo) {
		logger.info("getSellerProductList sellerNo=" + sellerNo);
		Map<String, Object> result = new HashMap<String, Object>();
		
		List<SellerProductResponse> sellerProductList = sellerService.getSellerProductList(sellerNo);
		
		result.put("sellerProductList", sellerProductList);
		result.put("message", "SELLER_PRODUCT_FETCH_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	// 제품 추가
	@PostMapping("/product")
	public ResponseEntity<Map<String, Object>> addProduct(@RequestAttribute("sellerNo") Integer sellerNo, 
		 	@Valid @ModelAttribute AddProductRequest product) {
		logger.info("sellerAddProduct " + product);
		Map<String, Object> result = new HashMap<String, Object>();
		
		sellerService.addProduct(product, sellerNo);

		result.put("message", "SELLER_PRODUCT_SAVE_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	// 제품 수정
	@PutMapping("/product")
	public ResponseEntity<Map<String, Object>> updateProduct(@RequestAttribute("sellerNo") Integer sellerNo, 
			@Valid @ModelAttribute UpdateProductRequest product) {
		logger.info("sellerUpdateProduct " + product);
		Map<String, Object> result = new HashMap<String, Object>();
		
		sellerService.updateProduct(product, sellerNo);

		result.put("message", "SELLER_PRODUCT_UPDATE_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	// 제품 상세보기
	@GetMapping("/product/detail/{productId}")
	public ResponseEntity<Map<String, Object>> getProductDetail(@RequestAttribute("sellerNo") Integer sellerNo, 
			@PathVariable("productId") int productId) {
		logger.info("getProductDetail " + productId);
		Map<String, Object> result = new HashMap<String, Object>();
		
		// 실제리스트에 뜨는 예시도 보여주고
		// 해당 리뷰 보여주고
		// 해당 QnA 보여주고
		// 판매자 제품에 적용 가능한 쿠폰조회 getProductAvailableCoupons

		result.put("message", "SELLER_PRODUCT_DETAIL_FETCH_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	// 제품 상세 사진수정
//	@GetMapping("/product/detail")
//	public ResponseEntity<Map<String, Object>> setProductDetailImages(@RequestParam("productId") String productId) {
//		logger.info("getSellerProductList");
//		Map<String, Object> result = new HashMap<String, Object>();
//		
//
////		result.put("sellerProductList", sellerProductList);
//		result.put("message", "SELLER_PRODUCT_DETAIL_FETCH_SUCCESS");
//		return new ResponseEntity<>(result, HttpStatus.OK);
//	}
	// 제품 옵션 추가
	@PostMapping("/product/option")
	public ResponseEntity<Map<String, Object>> setSellerProductOption(@Valid @ModelAttribute AddProductOptionRequest productOption,
			@RequestAttribute("sellerNo") Integer sellerNo) {
		logger.info("setSellerProductOption " + productOption);
		Map<String, Object> result = new HashMap<String, Object>();
		
		sellerService.addProductOption(productOption, sellerNo);

		result.put("message", "SELLER_PRODUCT_OPTION_ADD_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	// 제품 옵션 수정
	@PutMapping("/product/option")
	public ResponseEntity<Map<String, Object>> updateSellerProductOption(@ModelAttribute UpdateProductOptionRequest productOption) {
		logger.info("updateSellerProductOption " + productOption);
		Map<String, Object> result = new HashMap<String, Object>();

		sellerService.updateProductOption(productOption);

		result.put("message", "SELLER_PRODUCT_OPTION_UPDATE_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	// 쿠폰 조회
	@GetMapping("/coupon")
	public ResponseEntity<Map<String, Object>> getSellerCouponList(@RequestAttribute("sellerNo") Integer sellerNo) {
		logger.info("getSellerCoupon "+ sellerNo);
		Map<String, Object> result = new HashMap<String, Object>();
		
		List<Coupon> couponList = sellerService.getSellerCouponList(sellerNo);
		
		result.put("couponList", couponList);
		result.put("message", "SELLER_COUPON_FETCH_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	// 쿠폰 등록
	@PostMapping("/coupon")
	public ResponseEntity<Map<String, Object>> addCoupon(@ModelAttribute Coupon coupon) {
		logger.info("addCoupon "+ coupon);
		Map<String, Object> result = new HashMap<String, Object>();
		
		sellerService.addCoupon(coupon);
		
		result.put("message", "SELLER_COUPON_CREATE_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	// 쿠폰 상태변경
	@PostMapping("/coupon/status")
	public ResponseEntity<Map<String, Object>> disabledCoupon(@ModelAttribute Coupon coupon) {
		logger.info("disabledCoupon "+ coupon.getCouponId());
		Map<String, Object> result = new HashMap<String, Object>();
		
		sellerService.updateCouponStatus(coupon);
		
		result.put("message", "SELLER_COUPON_STATUS_UPDATE_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	// 판매자 해당 쿠폰 허용제품 조회
	@GetMapping("/coupon/allowed")
	public ResponseEntity<Map<String, Object>> getSellerCouponAllow(@RequestParam("couponId") String couponId) {
		logger.info("getSellerCouponAllow "+ couponId);
		Map<String, Object> result = new HashMap<String, Object>();
		
		List<SellerCouponAllowedProductDTO> CouponAllowedProductList = sellerService.getSellerCouponAllow(couponId);
		
		result.put("CouponAllowedProductList", CouponAllowedProductList);
		result.put("message", "SELLER_COUPON_ALLOWED_FETCH_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	// 판매자 해당 쿠폰 허용제품 변경
	@PostMapping("/coupon/allowed")
	public ResponseEntity<Map<String, Object>> setSellerCouponAllow(@RequestParam("couponId") String couponId, @RequestParam("productIds") List<Integer> productIds) {
		logger.info("getSellerCouponAllow "+ couponId + " => " + productIds);
		Map<String, Object> result = new HashMap<String, Object>();
		
		sellerService.setSellerCouponAllow(couponId, productIds);
		
		result.put("message", "SELLER_COUPON_ALLOWED_SAVE_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	// 해당 쿠폰을 유저에게 발행하기
	@PostMapping("/coupon/user-coupon")
	public ResponseEntity<Map<String, Object>> issueCouponsToUsers(@RequestParam("couponId") String couponId, @RequestParam("userIds") List<String> userIds) {
		logger.info("issueCouponsToUsers "+ couponId + " : " + userIds);
		Map<String, Object> result = new HashMap<String, Object>();
		
		sellerService.issueCouponsToUsers(couponId, userIds);
		
		result.put("message", "SELLER_COUPON_ISSUE_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	// 판매자와 관련된 회원 조회(내 상품을 보거나 위시하거나 장바구니에 넣거나 즐겨찾기한) 
	@GetMapping("/user/interesting")
	public ResponseEntity<Map<String, Object>> getSellerInterestingUser(@RequestAttribute("sellerNo") Integer sellerNo) {
		logger.info("getSellerInterestingUser "+ sellerNo);
		Map<String, Object> result = new HashMap<String, Object>();
		
		List<ProductViewCountDTO> productViewCountList = sellerService.getProductViewCountList(sellerNo);
		
		List<ProductWishCountDTO> productWishCountList = sellerService.getProductWishCountList(sellerNo);
		
		List<userInBookmarkDTO> brandBookmarkList = sellerService.getBrandBookmarkList(sellerNo);
		
		List<UserInCartCountDTO> userInCartCountList = sellerService.getUserInCartCountList(sellerNo);
		
		// 주문액수, 주문 갯수 가져오기
		
		result.put("productViewCountList", productViewCountList);
		result.put("productWishCountList", productWishCountList);
		result.put("brandBookmarkList", brandBookmarkList);
		result.put("userInCartCountList", userInCartCountList);
		result.put("message", "SELLER_INTERESTING_USER_FETCH_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	// 유저쿠폰사용내역 조회
	@GetMapping("/coupon/user-coupon")
	public ResponseEntity<Map<String, Object>> getSellerUsercouponUsed(@RequestAttribute("sellerNo") String sellerNo) {
		logger.info("getSellerUsercouponUsed "+ sellerNo);
		Map<String, Object> result = new HashMap<String, Object>();
		
		result.put("message", "SELLER_USER_COUPON_USED_FETCH_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	// 유저쿠폰사용내역 상세조회 - 어느주문에 사용했는지
	
		
}
