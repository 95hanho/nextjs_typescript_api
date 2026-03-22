package me._hanho.nextjs_shop.buy;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import me._hanho.nextjs_shop.buy.BuyService.HoldTryResult;
import me._hanho.nextjs_shop.common.exception.BusinessException;
import me._hanho.nextjs_shop.common.exception.ErrorCode;
import me._hanho.nextjs_shop.model.StockHoldCoupon;
import me._hanho.nextjs_shop.model.UserAddress;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bapi/buy")
public class BuyController {
	
	/**
	 * ======= 해결해야할 문제 =========
	 * 판매자 정지 시 처리하기 where절 추가
	 * 상품 가격 price -> origin_price, final_price로 나눠짐.
	 * 
	 */
	
	private static final Logger logger = LoggerFactory.getLogger(BuyController.class);
	
	private final BuyService buyService;
	
	// 상품 확인 및 점유 => 이후 (구매페이지이동)
	// FE : 10분 안에 아무 동작도 없고 결제도 안하고 하면 알람
	@PostMapping("/stock-hold")
	public ResponseEntity<Map<String, Object>> saleStatusCheck(
	        @RequestBody BuyCheckRequest buyCheck,
	        @RequestAttribute(value="userNo", required=false) Integer userNo) {
		if (userNo == null) throw new BusinessException(ErrorCode.AUTHENTICATION_REQUIRED);
	    Map<String, Object> result = new HashMap<>();

        HoldTryResult res = buyService.preparePurchaseWithHold(buyCheck, userNo);

        if (!res.isOk()) {
            throw new BusinessException(ErrorCode.STOCK_HOLD_FAILED);
        }

        result.put("message", "STOCK_HOLD_SUCCESS");
        result.put("holds", res.getHolds());
        return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	// 점유 연장 (여러 holdId 배치)
    @PostMapping("/stock-hold/extend")
    public ResponseEntity<Map<String, Object>> extendStockHold(
    		@RequestAttribute(value="userNo", required=false) Integer userNo) {
    	if (userNo == null) throw new BusinessException(ErrorCode.AUTHENTICATION_REQUIRED);
        logger.info("extendStockHold userNo=", userNo);
        Map<String, Object> result = new HashMap<>();
        
        List<Integer> holdIds = buyService.selectAllActiveHoldsByUser(userNo);
        
        int updated = buyService.extendHolds(holdIds, userNo);
        int requested = holdIds == null ? 0 : holdIds.size();
        
        result.put("holdIds", holdIds);
        result.put("requestedCount", requested);
        result.put("updatedCount", updated);
        // 400 (잘못된 요청)
        if (requested == 0) {
    	    throw new BusinessException(ErrorCode.HOLD_IDS_REQUIRED);
    	}
        // 성공
    	if (updated == requested) {
    	    result.put("message", "STOCK_HOLD_EXTEND_SUCCESS");
    	    return new ResponseEntity<>(result, HttpStatus.OK);
    	}
    	// updated == 0 → “전부 무효” (만료/판매불가/해제 등) STOCK_HOLD_EXPIRED
    	// 0 < updated < requested → “부분 무효” (일부만 유효) STOCK_HOLD_PARTIAL_EXPIRED
    	if(updated == 0) throw new BusinessException(ErrorCode.STOCK_HOLD_EXPIRED);
    	throw new BusinessException(ErrorCode.STOCK_HOLD_PARTIAL_EXPIRED);
    }
    
    // 점유 해제 (여러 holdId 배치)
    // DELETE /bapi/buy/stock-hold   (body: {"holdIds":[...]} )
    @DeleteMapping("/stock-hold")
    public ResponseEntity<Map<String, Object>> release(
    		@RequestAttribute(value="userNo", required=false) Integer userNo) {
    	if (userNo == null) throw new BusinessException(ErrorCode.AUTHENTICATION_REQUIRED);
        logger.info("release holds");
        Map<String, Object> result = new HashMap<>();
        
        List<Integer> holdIds = buyService.selectAllActiveHoldsByUser(userNo);
        
        int released = buyService.releaseHolds(holdIds, userNo);
        
        result.put("holdIds", holdIds);
        result.put("requestedCount", holdIds == null ? 0 : holdIds.size());
        result.put("releasedCount", released);
        result.put("message", "success");
        return ResponseEntity.ok(result);
    }

	// 점유 쿠폰 추가/삭제
    @PostMapping("/stock-hold/coupon")
    public ResponseEntity<Map<String, Object>> manageStockHoldCoupon(
			@ModelAttribute ManageStockHoldCouponRequest request,
    		@RequestAttribute(value="userNo", required=false) Integer userNo) {
    	if (userNo == null) throw new BusinessException(ErrorCode.AUTHENTICATION_REQUIRED);
        logger.info("manageStockHoldCoupon userNo=", userNo);
        Map<String, Object> result = new HashMap<>();
        
		if(request.getIsAdd()) {
			buyService.addHoldCoupon(request.getHoldId(), request.getUserCouponId(), userNo);
		} else {
			buyService.removeHoldCoupon(request.getHoldCouponId(), userNo);
		}
        
		result.put("message", "success");
        return ResponseEntity.ok(result);
    }
    
    /**
     * 제품판매여부, 판매자 중단여부 확인 후 바꼈을 지 어떻게 할지 넣어야할듯... 
     */
	// 점유 중인 상품 및 사용 가능 쿠폰 조회(결제화면)
	@GetMapping("/pay")
    public ResponseEntity<Map<String, Object>> getStockHoldProduct(
    		@RequestAttribute(value="userNo", required=false) Integer userNo) {
		if (userNo == null) throw new BusinessException(ErrorCode.AUTHENTICATION_REQUIRED);
        logger.info("getPayBefore : " + userNo);
        Map<String, Object> body = new HashMap<>();
        
        List<OrderStockResponse> stockHoldProductList = buyService.getStockHoldProductList(userNo);

		if(stockHoldProductList == null || stockHoldProductList.isEmpty()) {
			throw new BusinessException(ErrorCode.NO_ACTIVE_HOLDS);
		}

		List<Integer> holdIds = new java.util.ArrayList<>();
		List<Integer> productIds = new java.util.ArrayList<>();
		for (OrderStockResponse os : stockHoldProductList) {
			holdIds.add(os.getHoldId());
			productIds.add(os.getProductId());
		}
		// 중복 제거
		productIds = productIds.stream().distinct().collect(Collectors.toList());

		// 점유 제품들에 대한 이용가능 장바구니 쿠폰 조회
		List<AvailableCartCouponAtBuyResponse> availableCartCoupons = buyService.getAvailableCartCouponsAtBuy(productIds, userNo);
		// 점유 제품들에 대한 이용가능 판매자 쿠폰 조회
		List<AvailableSellerCouponAtBuyResponse> availableSellerCoupons = buyService.getAvailableSellerCouponsAtBuy(productIds, userNo);
		// 점유 제품들에 대한 초기 쿠폰 선택값
		List<StockHoldCoupon> holdCoupons = buyService.getInitialHoldCoupons(holdIds);

		// 기본 배송지 조회
		DefaultAddressResponse defaultAddress = buyService.getDefaultAddress(userNo); 
        
        body.put("stockHoldProductList", stockHoldProductList);
        body.put("availableCartCoupons", availableCartCoupons);
        body.put("availableSellerCoupons", availableSellerCoupons);
        body.put("holdCoupons", holdCoupons);
		body.put("defaultAddress", defaultAddress);
        body.put("message", "success");
        return ResponseEntity.ok(body);
    }
	

	// 상품 쿠폰, 마일리지, 배송비 여부의 변경에 따라 가격계산해서 보여줌.(결제화면)
	@PostMapping("pay-price")
	public ResponseEntity<Map<String, Object>> payPrice(@RequestBody PayPriceRequest payPriceRequest, 
			@RequestAttribute(value="userNo", required=false) Integer userNo) {
		if (userNo == null) throw new BusinessException(ErrorCode.AUTHENTICATION_REQUIRED);
	    logger.info("pay-price : {}", payPriceRequest);
	    Map<String, Object> result = new HashMap<>();
	    
	    List<ProductWithCouponResponse> items =
	        buyService.getProductWithCoupons(payPriceRequest.getProducts(), userNo);
	    
	    // 1) 각 상품 쿠폰 먼저 적용
	    PriceCalculatorService.applyDiscounts(items);

	    BigDecimal zero = BigDecimal.ZERO;
	    
	    BigDecimal couponDiscountTotal = items.stream()
	            .map(ProductWithCouponResponse::getDiscountAmount)
	            .filter(Objects::nonNull)
	            .reduce(zero, BigDecimal::add); // 각각 상품 쿠폰으로만 할인된 가격 

	    BigDecimal couponFinalTotal = items.stream()
	            .map(ProductWithCouponResponse::getResultPrice)
	            .filter(Objects::nonNull)
	            .reduce(zero, BigDecimal::add); // 각각 상품 쿠폰으로만 할인받은 최종가격  
	    
	    // 2) 공용 쿠폰(mainCoupon) 적용 (상품쿠폰 이후, 마일리지 이전)
	    AvailableCouponResponse mainCoupon = payPriceRequest.getCommonCoupon();
	    BigDecimal mainCouponDiscount = PriceCalculatorService.calcCommonCouponDiscount(couponFinalTotal, mainCoupon);

	    BigDecimal afterMainCouponTotal = couponFinalTotal.subtract(mainCouponDiscount);
	    
	    // 3) 마일리지 적용
	    BigDecimal requestedMileage = BigDecimal.valueOf(Math.max(0, payPriceRequest.getUseMileage())); // 요청된 마일리지
	    BigDecimal mileageApplied = requestedMileage.min(afterMainCouponTotal); // 적용된 마일리지

	    // 4) 배송비 판단(마일리지까지 적용한 금액 기준)
	    BigDecimal totalFinalBeforeDelivery = afterMainCouponTotal.subtract(mileageApplied);

	    // 🚚 배송비 계산
	    BigDecimal deliveryFee = totalFinalBeforeDelivery.compareTo(new BigDecimal("20000")) >= 0
	            ? zero
	            : new BigDecimal("3000");

	    BigDecimal totalFinal = totalFinalBeforeDelivery.add(deliveryFee);  // 총 금액
	    
	    // 총 할인 = (각 상품쿠폰 합) + (공용쿠폰 할인) + (마일리지)
	    BigDecimal totalDiscount = couponDiscountTotal.add(mainCouponDiscount).add(mileageApplied);

	    
	    // 응답
	    result.put("items", items);
	    result.put("onlyEachCouponDiscountTotal", couponDiscountTotal); // 각각 상품 쿠폰으로만 할인된 가격 
	    result.put("onlyEachCouponFinalTotal",   couponFinalTotal); // 각각 상품 쿠폰으로만 할인받은 최종가격
	    result.put("mainCouponDiscount",         mainCouponDiscount);     // 공용쿠폰 할인
	    result.put("afterMainCouponTotal",       afterMainCouponTotal);   // 공용쿠폰 적용 후 합계
	    result.put("mileageRequested", requestedMileage); // 요청된 마일리지
	    result.put("mileageApplied",  mileageApplied); // 적용된 마일리지
	    result.put("deliveryFee",     deliveryFee); // 배송비
	    result.put("totalDiscount",   totalDiscount); // 총 할인비(쿠폰할인 + 마일리지)
	    result.put("totalFinal",      totalFinal); // 총 금액
	    result.put("message", "success");
	    return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	// 상품 구매/결제
	@PostMapping("pay") // 어떤 제품을 구매하는지, 쿠폰을 어떤걸 적용시켰는지, 배송지는 어디인지, 결제수단은 어떻게되는지, 적립금 사용액, 
	public ResponseEntity<Map<String, Object>> pay(
			@RequestBody PayRequest payRequest, 
			@RequestAttribute(value="userNo", required=false) Integer userNo) {
		if (userNo == null) throw new BusinessException(ErrorCode.AUTHENTICATION_REQUIRED);
		logger.info("pay : {}" + payRequest);
		Map<String, Object> result = new HashMap<String, Object>();
		
		buyService.pay(payRequest, userNo);
		
		result.put("message", "success");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
}
