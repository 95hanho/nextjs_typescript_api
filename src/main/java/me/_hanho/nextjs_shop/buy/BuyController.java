package me._hanho.nextjs_shop.buy;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import me._hanho.nextjs_shop.buy.BuyService.HoldTryResult;
import me._hanho.nextjs_shop.model.Coupon;

@RestController
@RequestMapping("/bapi/buy")
public class BuyController {
	
	private static final Logger logger = LoggerFactory.getLogger(BuyController.class);
	
	@Autowired
	private BuyService buyService;
	
	// 상품 확인 및 점유(구매페이지이동)
	// FE : 10분 안에 아무 동작도 없고 결제도 안하고 하면 알람
	@PostMapping("/status")
	public ResponseEntity<Map<String, Object>> saleStatusCheck(@RequestBody BuyCheckRequest buyCheck) {
		logger.info("saleStatusCheck {}", buyCheck);
	    Map<String, Object> result = new HashMap<>();

//	    HoldTryResult res = buyService.tryHoldAllOrNothing(buyCheck); // 전부 가능 시 홀드 생성
	    HoldTryResult res = buyService.tryHoldUpsertAllOrNothing(buyCheck);

	    result.put("ok", res.isOk());
	    result.put("holds", res.getHolds()); // [{product_detail_id, hold_id}]
	    result.put("msg", "success");
	    return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	// 점유 연장 (여러 holdId 배치)
    @PostMapping("/stock-hold/extend")
    public ResponseEntity<Map<String, Object>> extendStockHold(@RequestBody HoldBatchRequest req) {
        logger.info("extendStockHold {}", req);
        int updated = buyService.extendHolds(req.getHoldIds());
        Map<String, Object> body = new HashMap<>();
        body.put("requestedIds", req.getHoldIds());
        body.put("updatedCount", updated);
        body.put("requestedCount", req.getHoldIds() == null ? 0 : req.getHoldIds().size());
        body.put("msg", "success");
        return ResponseEntity.ok(body);
    }
    
    // 점유 해제 (여러 holdId 배치)
    // DELETE /bapi/buy/stock-hold   (body: {"holdIds":[...]} )
    @DeleteMapping("/stock-hold")
    public ResponseEntity<Map<String, Object>> release(@RequestBody HoldBatchRequest req) {
        logger.info("release holds: {}", req);
        int released = buyService.releaseHolds(req.getHoldIds());
        Map<String, Object> body = new HashMap<>();
        body.put("requestedIds", req.getHoldIds());
        body.put("releasedCount", released);
        body.put("requestedCount", req.getHoldIds() == null ? 0 : req.getHoldIds().size());
        body.put("msg", "success");
        return ResponseEntity.ok(body);
    }
    
	// 결제 바로 전 상품 및 필요정보들 조회 점유하고 있는 상품조회
	@GetMapping("/pay")
    public ResponseEntity<Map<String, Object>> getPayBefore(@RequestParam("user_id") String user_id) {
        logger.info("getPayBefore : " + user_id);
        Map<String, Object> body = new HashMap<>();
        
        List<AvailableCoupon> availableCouponList = null;
        List<OrderStockDTO> orderStock = buyService.getOrderStock(user_id);
        List<Integer> productIds = orderStock.stream()
                .map(OrderStockDTO::getProduct_id)
                .distinct()
                .collect(Collectors.toList());
        
        if(productIds.size() > 0) {
        	availableCouponList = buyService.getAvailableCoupon(productIds, user_id);
        }
        
        body.put("orderStock", orderStock);
        body.put("availableCouponList", availableCouponList);
        body.put("msg", "success");
        return ResponseEntity.ok(body);
    }
	
	// 결제 바로 전 상품 쿠폰, 마일리지, 배송비 여부의 변경에 따라 가격계산해서 보여줌.
	@PostMapping("pay-price")
	public ResponseEntity<Map<String, Object>> payPrice(@RequestBody PayPriceRequest payPriceRequest) {
	    logger.info("pay-price : {}", payPriceRequest);
	    Map<String, Object> result = new HashMap<>();
	    
	    List<ProductWithCouponsDTO> items =
	        buyService.getProductWithCoupons(payPriceRequest.getProducts(), payPriceRequest.getUser_id());
	    
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
	    result.put("msg", "success");
	    return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	// 상품 구매/결제
	@PostMapping("pay") // 어떤 제품을 구매하는지, 쿠폰을 어떤걸 적용시켰는지, 배송지는 어디인지, 결제수단은 어떻게되는지, 적립금 사용액, 
	public ResponseEntity<Map<String, Object>> pay(@RequestBody PayRequest payRequest) {
		logger.info("pay : {}" + payRequest);
		Map<String, Object> result = new HashMap<String, Object>();
		
		buyService.pay(payRequest);
		
		result.put("msg", "success");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
}
