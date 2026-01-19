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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import me._hanho.nextjs_shop.buy.BuyService.HoldTryResult;

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
	        @RequestAttribute("userNo") Integer userNo) {

	    Map<String, Object> result = new HashMap<>();
	    buyCheck.setUserNo(userNo);

	    try {
	        HoldTryResult res = buyService.tryHoldUpsertAllOrNothing(buyCheck);

	        if (!res.isOk()) {
	            result.put("message", "STOCK_HOLD_FAILED");
	            return new ResponseEntity<>(result, HttpStatus.CONFLICT);
	        }

	        result.put("message", "STOCK_HOLD_SUCCESS");
	        result.put("holds", res.getHolds());
	        return new ResponseEntity<>(result, HttpStatus.OK);

	    } catch (HoldFailedException e) {
	        // 여기서 메시지를 e.getMessage()로 매핑해도 되고, 고정해도 됨
	        result.put("message", e.getMessage()); // "HOLD_UPSERT_INCOMPLETE"
	        return new ResponseEntity<>(result, HttpStatus.CONFLICT);
	    }
	}
	
	// 점유 연장 (여러 holdId 배치)
    @PostMapping("/stock-hold/extend")
    public ResponseEntity<Map<String, Object>> extendStockHold(@RequestBody HoldBatchRequest req,
    		@RequestAttribute("userNo") Integer userNo) {
        logger.info("extendStockHold {}", req);
        Map<String, Object> result = new HashMap<>();
        
        int updated = buyService.extendHolds(req.getHoldIds(), userNo);
        int requested = req.getHoldIds() == null ? 0 : req.getHoldIds().size();
        
        result.put("holdIds", req.getHoldIds());
        result.put("requestedCount", requested);
        result.put("updatedCount", updated);
        // 400 (잘못된 요청)
        if (requested == 0) {
        	result.put("message", "HOLD_IDS_REQUIRED");
    	    return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    	}
        // 성공
    	if (updated == requested) {
    	    result.put("message", "STOCK_HOLD_EXTEND_SUCCESS");
    	    return new ResponseEntity<>(result, HttpStatus.OK);
    	}
    	// updated == 0 → “전부 무효” (만료/판매불가/해제 등) STOCK_HOLD_EXPIRED
    	// 0 < updated < requested → “부분 무효” (일부만 유효) STOCK_HOLD_PARTIAL_EXPIRED
    	result.put("message", updated == 0 ? "STOCK_HOLD_EXPIRED" : "STOCK_HOLD_PARTIAL_EXPIRED");
    	return new ResponseEntity<>(result, HttpStatus.CONFLICT);
        
    }
    
    // 점유 해제 (여러 holdId 배치)
    // DELETE /bapi/buy/stock-hold   (body: {"holdIds":[...]} )
    @DeleteMapping("/stock-hold")
    public ResponseEntity<Map<String, Object>> release(@RequestParam("holdIds") List<Integer> holdIds,
    		@RequestAttribute("userNo") Integer userNo) {
        logger.info("release holds: {}", holdIds);
        Map<String, Object> result = new HashMap<>();
        int released = buyService.releaseHolds(holdIds, userNo);
        
        result.put("holdIds", holdIds);
        result.put("requestedCount", holdIds == null ? 0 : holdIds.size());
        result.put("releasedCount", released);
        result.put("message", "success");
        return ResponseEntity.ok(result);
    }
    
    /**
     * 제품판매여부, 판매자 중단여부 확인 후 바꼈을 지 어떻게 할지 넣어야할듯... 
     */
	// 점유 중인 상품 및 사용 가능 쿠폰 조회(결제화면)
	@GetMapping("/pay")
    public ResponseEntity<Map<String, Object>> getStockHoldProduct(@RequestAttribute("userNo") Integer userNo) {
        logger.info("getPayBefore : " + userNo);
        Map<String, Object> body = new HashMap<>();
        
        List<AvailableCoupon> availableCouponList = null;
        List<OrderStockDTO> orderStock = buyService.getOrderStock(userNo);
        List<Integer> productIds = orderStock.stream()
                .map(OrderStockDTO::getProductId)
                .distinct()
                .collect(Collectors.toList());
        
        if(productIds.size() > 0) {
        	availableCouponList = buyService.getAvailableCoupon(productIds, userNo);
        }
        
        body.put("orderStock", orderStock);
        body.put("availableCouponList", availableCouponList);
        body.put("message", "success");
        return ResponseEntity.ok(body);
    }
	
	// 상품 쿠폰, 마일리지, 배송비 여부의 변경에 따라 가격계산해서 보여줌.(결제화면)
	@PostMapping("pay-price")
	public ResponseEntity<Map<String, Object>> payPrice(@RequestBody PayPriceRequest payPriceRequest, 
			@RequestAttribute("userNo") Integer userNo) {
	    logger.info("pay-price : {}", payPriceRequest);
	    Map<String, Object> result = new HashMap<>();
	    
	    payPriceRequest.setUserNo(userNo);
	    List<ProductWithCouponsDTO> items =
	        buyService.getProductWithCoupons(payPriceRequest.getProducts(), payPriceRequest.getUserNo());
	    
	    // 1) 각 상품 쿠폰 먼저 적용
	    PriceCalculatorService.applyDiscounts(items);

	    BigDecimal zero = BigDecimal.ZERO;
	    
	    BigDecimal couponDiscountTotal = items.stream()
	            .map(ProductWithCouponsDTO::getDiscountAmount)
	            .filter(Objects::nonNull)
	            .reduce(zero, BigDecimal::add); // 각각 상품 쿠폰으로만 할인된 가격 

	    BigDecimal couponFinalTotal = items.stream()
	            .map(ProductWithCouponsDTO::getFinalPrice)
	            .filter(Objects::nonNull)
	            .reduce(zero, BigDecimal::add); // 각각 상품 쿠폰으로만 할인받은 최종가격  
	    
	    // 2) 공용 쿠폰(mainCoupon) 적용 (상품쿠폰 이후, 마일리지 이전)
	    AvailableCoupon mainCoupon = payPriceRequest.getCommonCoupon();
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
	public ResponseEntity<Map<String, Object>> pay(@RequestBody PayRequest payRequest, @RequestAttribute("userNo") Integer userNo) {
		logger.info("pay : {}" + payRequest);
		Map<String, Object> result = new HashMap<String, Object>();
		
		payRequest.setUserNo(userNo);
		buyService.pay(payRequest);
		
		result.put("message", "success");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
}
