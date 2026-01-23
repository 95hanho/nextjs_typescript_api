package me._hanho.nextjs_shop.seller;

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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me._hanho.nextjs_shop.auth.AuthService;
import me._hanho.nextjs_shop.auth.ReToken;
import me._hanho.nextjs_shop.auth.TokenService;
import me._hanho.nextjs_shop.common.exception.BusinessException;
import me._hanho.nextjs_shop.common.exception.ErrorCode;

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
	        @RequestAttribute(value="sellerNo", required=false) Integer sellerNo,
	        @RequestParam("refreshToken") String refreshToken,
	        @RequestHeader("user-agent") String userAgent,
	        @RequestHeader("x-forwarded-for") String forwardedFor
	) {
		if (sellerNo == null) throw new BusinessException(ErrorCode.AUTHENTICATION_REQUIRED);
		logger.info("insertToken refreshToken : " + refreshToken.substring(refreshToken.length() - 10) + ", sellerNo : " + sellerNo + 
				", user-agent : " + userAgent + ", x-forwarded-for : " + forwardedFor);
		Map<String, Object> result = new HashMap<String, Object>();
		
	    tokenService.parseJwtRefreshToken(refreshToken); // 여기서 유효하지 않으면 예외 던지게
	
	    String ipAddress = forwardedFor != null ? forwardedFor : "unknown";
	    SellerToken token = SellerToken.builder().connectIp(ipAddress).connectAgent(userAgent).refreshToken(refreshToken).build();
	
	    sellerService.insertToken(token, sellerNo);
	
	    result.put("message", "SELLER_TOKEN_INSERT_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	// 로그인 토큰 수정(재저장)
	@PostMapping("/token/refresh")
	public ResponseEntity<Map<String, Object>> updateToken(
	        @RequestParam("beforeToken") String beforeToken,
	        @RequestParam("refreshToken") String refreshToken,
	        @RequestHeader("user-agent") String userAgent,
	        @RequestHeader("x-forwarded-for") String forwardedFor
	) {
		logger.info("updateToken beforeToken : " + beforeToken.substring(beforeToken.length() - 10) + 
				", refreshToken : " + refreshToken.substring(refreshToken.length() - 10) + ", user-agent : " + userAgent + 
				", x-forwarded-for : " + forwardedFor);
		Map<String, Object> result = new HashMap<String, Object>();
		
	    tokenService.parseJwtRefreshToken(beforeToken);
	    tokenService.parseJwtRefreshToken(refreshToken);

	    String ipAddress = forwardedFor != null ? forwardedFor : "unknown";
	    ReToken token = ReToken.builder().connectIp(ipAddress).connectAgent(userAgent).refreshToken(refreshToken).beforeToken(beforeToken).build();

	    authService.updateToken(token);

	    Integer sellerNo = sellerService.getSellerNoByToken(token);
	    if (sellerNo == null) {
	        throw new BusinessException(ErrorCode.WRONG_TOKEN);
	    }

	    result.put("sellerNo", sellerNo);
		result.put("message", "TOKEN_UPDATE_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	// 판매자 정보조회
	@GetMapping
	public ResponseEntity<Map<String, Object>> getSeller(@RequestAttribute(value="sellerNo", required=false) Integer sellerNo) {
		if (sellerNo == null) throw new BusinessException(ErrorCode.AUTHENTICATION_REQUIRED);
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
	public ResponseEntity<Map<String, Object>> getSellerProductList(@RequestAttribute(value="sellerNo", required=false) Integer sellerNo) {
		if (sellerNo == null) throw new BusinessException(ErrorCode.AUTHENTICATION_REQUIRED);
		logger.info("getSellerProductList sellerNo=" + sellerNo);
		Map<String, Object> result = new HashMap<String, Object>();
		
		List<SellerProductResponse> sellerProductList = sellerService.getSellerProductList(sellerNo);
		
		result.put("sellerProductList", sellerProductList);
		result.put("message", "SELLER_PRODUCT_FETCH_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	// 제품 추가
	@PostMapping("/product")
	public ResponseEntity<Map<String, Object>> addProduct(@RequestAttribute(value="sellerNo", required=false) Integer sellerNo, 
		 	@Valid @ModelAttribute AddProductRequest product) {
		if (sellerNo == null) throw new BusinessException(ErrorCode.AUTHENTICATION_REQUIRED);
		logger.info("sellerAddProduct " + product);
		Map<String, Object> result = new HashMap<String, Object>();
		
		sellerService.addProduct(product, sellerNo);

		result.put("message", "SELLER_PRODUCT_SAVE_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	// 제품 수정
	@PutMapping("/product")
	public ResponseEntity<Map<String, Object>> updateProduct(@RequestAttribute(value="sellerNo", required=false) Integer sellerNo, 
			@Valid @ModelAttribute UpdateProductRequest product) {
		if (sellerNo == null) throw new BusinessException(ErrorCode.AUTHENTICATION_REQUIRED);
		logger.info("sellerUpdateProduct " + product);
		Map<String, Object> result = new HashMap<String, Object>();
		
		sellerService.updateProduct(product, sellerNo);

		result.put("message", "SELLER_PRODUCT_UPDATE_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	// 제품 상세보기
	@GetMapping("/product/detail/{productId}")
	public ResponseEntity<Map<String, Object>> getProductDetail(@RequestAttribute(value="sellerNo", required=false) Integer sellerNo, 
			@PathVariable("productId") int productId) {
		if (sellerNo == null) throw new BusinessException(ErrorCode.AUTHENTICATION_REQUIRED);
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
			@RequestAttribute(value="sellerNo", required=false) Integer sellerNo) {
		if (sellerNo == null) throw new BusinessException(ErrorCode.AUTHENTICATION_REQUIRED);
		logger.info("setSellerProductOption " + productOption);
		Map<String, Object> result = new HashMap<String, Object>();
		
		sellerService.addProductOption(productOption, sellerNo);

		result.put("message", "SELLER_PRODUCT_OPTION_ADD_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	// 제품 옵션 수정
	@PutMapping("/product/option")
	public ResponseEntity<Map<String, Object>> updateSellerProductOption(@Valid @ModelAttribute UpdateProductOptionRequest productOption,
			@RequestAttribute(value="sellerNo", required=false) Integer sellerNo) {
		if (sellerNo == null) throw new BusinessException(ErrorCode.AUTHENTICATION_REQUIRED);
		logger.info("updateSellerProductOption " + productOption);
		Map<String, Object> result = new HashMap<String, Object>();

		sellerService.updateProductOption(productOption, sellerNo);

		result.put("message", "SELLER_PRODUCT_OPTION_UPDATE_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	// 제품 옵션 삭제
	@DeleteMapping("/product/option/{productOptionId}")
	public ResponseEntity<Map<String, Object>> deleteSellerProductOption(@PathVariable("productOptionId") Integer productOptionId, 
			@RequestAttribute(value="sellerNo", required=false) Integer sellerNo) {
		if (sellerNo == null) throw new BusinessException(ErrorCode.AUTHENTICATION_REQUIRED);
		logger.info("deleteSellerProductOption " + productOptionId);
		Map<String, Object> result = new HashMap<String, Object>();

		sellerService.deleteProductOption(productOptionId, sellerNo);

		result.put("message", "SELLER_PRODUCT_OPTION_DELETE_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	// 쿠폰 조회
	@GetMapping("/coupon")
	public ResponseEntity<Map<String, Object>> getSellerCouponList(@RequestAttribute(value="sellerNo", required=false) Integer sellerNo) {
		if (sellerNo == null) throw new BusinessException(ErrorCode.AUTHENTICATION_REQUIRED);
		logger.info("getSellerCoupon "+ sellerNo);
		Map<String, Object> result = new HashMap<String, Object>();
		
		List<SellerCouponResponse> couponList = sellerService.getSellerCouponList(sellerNo);
		
		result.put("couponList", couponList);
		result.put("message", "SELLER_COUPON_FETCH_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	// 쿠폰 등록
	@PostMapping("/coupon")
	public ResponseEntity<Map<String, Object>> addCoupon(@Valid @ModelAttribute AddCouponRequest coupon,
			@RequestAttribute(value="sellerNo", required=false) Integer sellerNo) {
		if (sellerNo == null) throw new BusinessException(ErrorCode.AUTHENTICATION_REQUIRED);
		logger.info("addSellerCoupon "+ coupon);
		Map<String, Object> result = new HashMap<String, Object>();
		
		// discount_type에 따른 규칙
		if ("percentage".equals(coupon.getDiscountType())) {
		    if (coupon.getMaxDiscount() == null) {
		        throw new BusinessException(ErrorCode.COUPON_MAX_DISCOUNT_REQUIRED_FOR_PERCENTAGE);
		    }
		} else if ("fixed_amount".equals(coupon.getDiscountType())) {
		    if (coupon.getMaxDiscount() != null) {
		        throw new BusinessException(ErrorCode.COUPON_MAX_DISCOUNT_MUST_BE_NULL_FOR_FIXED_AMOUNT);
		    }
		} else {
		    throw new BusinessException(ErrorCode.COUPON_INVALID_DISCOUNT_TYPE);
		}
		
		sellerService.addCoupon(coupon, sellerNo);
		
		result.put("message", "SELLER_COUPON_CREATE_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	// 쿠폰 수정
	@PutMapping("/coupon")
	public ResponseEntity<Map<String, Object>> updateCoupon(@Valid @ModelAttribute UpdateCouponRequest coupon,
			@RequestAttribute(value="sellerNo", required=false) Integer sellerNo) {
		if (sellerNo == null) throw new BusinessException(ErrorCode.AUTHENTICATION_REQUIRED);
		logger.info("updateCoupon "+ coupon);
		Map<String, Object> result = new HashMap<String, Object>();
		
		// discount_type에 따른 규칙
		if ("percentage".equals(coupon.getDiscountType())) {
		    if (coupon.getMaxDiscount() == null) {
		        throw new BusinessException(ErrorCode.COUPON_MAX_DISCOUNT_REQUIRED_FOR_PERCENTAGE);
		    }
		} else if ("fixed_amount".equals(coupon.getDiscountType())) {
		    if (coupon.getMaxDiscount() != null) {
		        throw new BusinessException(ErrorCode.COUPON_MAX_DISCOUNT_MUST_BE_NULL_FOR_FIXED_AMOUNT);
		    }
		} else {
		    throw new BusinessException(ErrorCode.COUPON_INVALID_DISCOUNT_TYPE);
		}
		
		sellerService.updateCoupon(coupon, sellerNo);
		
		result.put("message", "SELLER_COUPON_UPDATE_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	// 쿠폰 삭제
	@DeleteMapping("/coupon/{couponId}")
	public ResponseEntity<Map<String, Object>> deleteCoupon(@PathVariable("couponId") Integer couponId,
			@RequestAttribute(value="sellerNo", required=false) Integer sellerNo) {
		if (sellerNo == null) throw new BusinessException(ErrorCode.AUTHENTICATION_REQUIRED);
		logger.info("deleteCoupon "+ couponId);
		Map<String, Object> result = new HashMap<String, Object>();
		
		sellerService.deleteCoupon(couponId, sellerNo);
		
		result.put("message", "SELLER_COUPON_DELETE_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	// 쿠폰 허용제품 조회 (+판매자제품이 너무 많으면 허용X제품 조회를 분리하기)
	@GetMapping("/coupon/allowed")
	public ResponseEntity<Map<String, Object>> getSellerProductCouponAllowed(@RequestParam("couponId") String couponId,
			@RequestAttribute(value="sellerNo", required=false) Integer sellerNo) {
		if (sellerNo == null) throw new BusinessException(ErrorCode.AUTHENTICATION_REQUIRED);
		logger.info("getSellerProductCouponAllowed "+ couponId);
		Map<String, Object> result = new HashMap<String, Object>();
		
		List<SellerProductCouponAllowed> CouponAllowedProductList = sellerService.getSellerCouponAllow(couponId, sellerNo);
		
		result.put("CouponAllowedProductList", CouponAllowedProductList);
		result.put("message", "SELLER_COUPON_ALLOWED_FETCH_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	// 쿠폰 허용제품 변경
	@PostMapping("/coupon/allowed")
	public ResponseEntity<Map<String, Object>> setSellerCouponAllow(
			@RequestParam("couponId") String couponId, 
			@RequestParam("productIds") List<Integer> productIds,
			@RequestParam("allow") Boolean allow, 
			@RequestAttribute(value="sellerNo", required=false) Integer sellerNo) {
		if (sellerNo == null) throw new BusinessException(ErrorCode.AUTHENTICATION_REQUIRED);
		logger.info("setSellerCouponAllow couponId={}, productIds={}, allow={}", couponId, productIds, allow);
		Map<String, Object> result = new HashMap<String, Object>();
		
		sellerService.setSellerCouponAllow(couponId, productIds, allow, sellerNo);
		
		result.put("message", "SELLER_COUPON_ALLOWED_SET_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	// 쿠폰을 유저에게 발행하기 - 특정 조건의 유저에게 발급하기!!! 아래 API와 연동하여서
	@PostMapping("/coupon/user-coupon")
	public ResponseEntity<Map<String, Object>> issueCouponsToUsers(@RequestParam("couponId") String couponId, @RequestParam("userNoList") List<Integer> userNoList) {
		logger.info("issueCouponsToUsers "+ couponId + " : " + userNoList);
		Map<String, Object> result = new HashMap<String, Object>();
		
		result.put("message", "SELLER_COUPON_ISSUE_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	// 판매자와 관련된 회원 조회(내 상품을 보거나 위시하거나 장바구니에 넣거나 즐겨찾기한) 
	@GetMapping("/user/interesting")
	public ResponseEntity<Map<String, Object>> getSellerInterestingUser(@RequestAttribute(value="sellerNo", required=false) Integer sellerNo) {
		if (sellerNo == null) throw new BusinessException(ErrorCode.AUTHENTICATION_REQUIRED);
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
	public ResponseEntity<Map<String, Object>> getSellerUsercouponUsed(@RequestAttribute(value="sellerNo", required=false) Integer sellerNo) {
		if (sellerNo == null) throw new BusinessException(ErrorCode.AUTHENTICATION_REQUIRED);
		logger.info("getSellerUsercouponUsed "+ sellerNo);
		Map<String, Object> result = new HashMap<String, Object>();
		
		result.put("message", "SELLER_USER_COUPON_USED_FETCH_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	// 유저쿠폰사용내역 상세조회 - 어느주문에 사용했는지
	
		
}
