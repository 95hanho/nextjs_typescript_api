package me._hanho.nextjs_shop.buy;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import me._hanho.nextjs_shop.buy.BuyService.HoldTryResult;
import me._hanho.nextjs_shop.buy.dto.AvailableCartCouponAtBuyResponse;
import me._hanho.nextjs_shop.buy.dto.AvailableSellerCouponAtBuyResponse;
import me._hanho.nextjs_shop.buy.dto.BuyCheckRequest;
import me._hanho.nextjs_shop.buy.dto.DefaultAddressResponse;
import me._hanho.nextjs_shop.buy.dto.LatestHoldInfo;
import me._hanho.nextjs_shop.buy.dto.ManageStockHoldCouponRequest;
import me._hanho.nextjs_shop.buy.dto.OrderStockResponse;
import me._hanho.nextjs_shop.buy.dto.PayRequest;
import me._hanho.nextjs_shop.common.exception.BusinessException;
import me._hanho.nextjs_shop.common.exception.ErrorCode;
import me._hanho.nextjs_shop.model.StockHoldCoupon;

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
		logger.info("[saleStatusCheck] userNo={}, buyCheck={}", userNo, buyCheck);
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
        logger.info("[extendStockHold] userNo={}", userNo);
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
        logger.info("[release] userNo={}", userNo);
        Map<String, Object> result = new HashMap<>();
        
        List<Integer> holdIds = buyService.selectAllActiveHoldsByUser(userNo);
        
        int released = buyService.releaseHolds(holdIds, userNo);
        
        result.put("holdIds", holdIds);
        result.put("requestedCount", holdIds == null ? 0 : holdIds.size());
        result.put("releasedCount", released);
        result.put("message", "success");
        return ResponseEntity.ok(result);
    }

	// 점유 쿠폰 추가
    @PostMapping("/stock-hold/coupon")
    public ResponseEntity<Map<String, Object>> addStockHoldCoupons(
			@RequestBody ManageStockHoldCouponRequest holdCouponRequests,
    		@RequestAttribute(value="userNo", required=false) Integer userNo) {
    	if (userNo == null) throw new BusinessException(ErrorCode.AUTHENTICATION_REQUIRED);
        logger.info("[addStockHoldCoupons] userNo={}", userNo);
        Map<String, Object> result = new HashMap<>();
        
		buyService.addStockHoldCoupons(holdCouponRequests.getHoldCoupons());
        
		result.put("message", "success");
        return ResponseEntity.ok(result);
    }

	// 점유 쿠폰 삭제
    @PutMapping("/stock-hold/coupon")
    public ResponseEntity<Map<String, Object>> deleteStockHoldCoupons(
			@RequestBody ManageStockHoldCouponRequest holdCouponRequests,
    		@RequestAttribute(value="userNo", required=false) Integer userNo) {
    	if (userNo == null) throw new BusinessException(ErrorCode.AUTHENTICATION_REQUIRED);
        logger.info("[deleteStockHoldCoupons] userNo={}", userNo);
        Map<String, Object> result = new HashMap<>();
        
		buyService.deleteStockHoldCoupons(holdCouponRequests.getHoldCoupons());
        
		result.put("message", "success");
        return ResponseEntity.ok(result);
    }
    
	// 점유 중인 상품 및 사용 가능 쿠폰 조회(결제화면)
	// 포트폴리오 단계에서는 userNo 기준으로 점유를 관리하며,
	// 다중 기기/세션 단위 구매 흐름 식별은 추후 hold_session_id 도입으로 확장 가능
	@GetMapping("/pay")
    public ResponseEntity<Map<String, Object>> getStockHoldProduct(
    		@RequestAttribute(value="userNo", required=false) Integer userNo) {
		if (userNo == null) throw new BusinessException(ErrorCode.AUTHENTICATION_REQUIRED);
        logger.info("[getStockHoldProduct] userNo={}", userNo);
        Map<String, Object> body = new HashMap<>();
        
        List<OrderStockResponse> stockHoldProductList = buyService.getStockHoldProductList(userNo);

		if(stockHoldProductList == null || stockHoldProductList.isEmpty()) {
			List<LatestHoldInfo> latestHolds = buyService.getLatestHoldsInfo(userNo);

			if (latestHolds == null || latestHolds.isEmpty()) {
				// 해당 계정으로 점유 자체가 없을 경우 - 메인 페이지로 보내기(잘 못 된 접근)
				throw new BusinessException(ErrorCode.NO_ACTIVE_HOLDS, "/");
			}

    		String returnUrl = latestHolds.get(0).getReturnUrl();

			boolean hasPaid = latestHolds.stream().anyMatch(h ->
				"PAY".equals(h.getStatus())
			);
			if (hasPaid) {
				// 이미 결제된 점유 - 메인페이지로 보내기
				throw new BusinessException(ErrorCode.ALREADY_PAID_HOLD, "/");
			}

			// 일단 점유가 된 상품이 있으면 모두 해제 (판매 중지됐는지, 만료됐는지 등은 상관없이 일단 해제)
			List<Integer> holdIds = latestHolds.stream().map(LatestHoldInfo::getHoldId).collect(Collectors.toList());
			buyService.releaseHolds(holdIds, userNo);

			// 만료된지 한 시간 이내인지
			boolean isExpireWithinOneHour = latestHolds.stream().anyMatch(h ->
				h.getExpiresAt() != null
				&& h.getExpiresAt().toLocalDateTime().isBefore(LocalDateTime.now())
				&& h.getExpiresAt().toLocalDateTime().isAfter(LocalDateTime.now().minusHours(1))
			);

			// 판매 중지된 상품이 포함된 점유 - 한시간 이내면 returnUrl로, 아니면 메인 페이지로
			// 판매 중지 상품 = 재고 0이거나, 진열 중지이거나, 판매 중지인 상품
			boolean hasSaleStopped = latestHolds.stream().anyMatch(h -> h.getStock() <= 0 || !h.isDisplayed() ||  h.isSaleStop());
			if (hasSaleStopped) {
				if(isExpireWithinOneHour) {
					throw new BusinessException(ErrorCode.PRODUCT_SALE_STOPPED, returnUrl);
				} else {
					throw new BusinessException(ErrorCode.PRODUCT_SALE_STOPPED, "/");
				}
			}

			// 판매자가 정지계정인 상품인 경우 - 한시간 이내면 returnUrl로, 아니면 메인 페이지로
			boolean hasSellerUnavailable = latestHolds.stream().anyMatch(h ->
				!"APPROVED".equals(h.getApprovalStatus())
			);
			if (hasSellerUnavailable) {
				if(isExpireWithinOneHour) {
					throw new BusinessException(ErrorCode.SELLER_UNAVAILABLE, returnUrl);
				} else {
					throw new BusinessException(ErrorCode.SELLER_UNAVAILABLE, "/");
				}
			}

			// HOLD상태이지만 이미 만료된 점유가 있는 경우 - 한시간 이내면 returnUrl로, 아니면 메인 페이지로
			boolean hasExpired = latestHolds.stream().anyMatch(h ->
				"HOLD".equals(h.getStatus())
				&& h.isActiveHold()
				&& h.getExpiresAt() != null
				&& h.getExpiresAt().toLocalDateTime().isBefore(LocalDateTime.now())
			);

			if (hasExpired) {
				// HOLD상태이지만 이미 만료된 점유 - 한시간 이내면 returnUrl로, 아니면 메인 페이지로
				if(isExpireWithinOneHour) {
					throw new BusinessException(ErrorCode.HOLD_EXPIRED, returnUrl);
				} else {
					throw new BusinessException(ErrorCode.HOLD_EXPIRED, "/");
				}
			}

			// 그 외의 경우 - 메인페이지로
			throw new BusinessException(ErrorCode.NO_ACTIVE_HOLDS, "/");
		}

		List<Integer> holdIds = new java.util.ArrayList<>();
		List<Integer> productIds = new java.util.ArrayList<>();
		for (OrderStockResponse os : stockHoldProductList) {
			holdIds.add(os.getHoldId());
			productIds.add(os.getProductId());
		}

		// 최초 진입 시 점유 연장
		buyService.extendHolds(holdIds, userNo);

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
	
	// 상품 구매/결제
	@PostMapping("pay") // 어떤 제품을 구매하는지, 쿠폰을 어떤걸 적용시켰는지, 배송지는 어디인지, 결제수단은 어떻게되는지, 적립금 사용액, 
	public ResponseEntity<Map<String, Object>> pay(
			@RequestBody PayRequest payRequest, 
			@RequestAttribute(value="userNo", required=false) Integer userNo) {
		if (userNo == null) throw new BusinessException(ErrorCode.AUTHENTICATION_REQUIRED);
		logger.info("[pay] userNo={}, payRequest={}", userNo, payRequest);
		Map<String, Object> result = new HashMap<String, Object>();
		
		// 파라미터 체크
		if(payRequest.getShippingAddress() == null || payRequest.getPaymentMethod() == null || payRequest.getHoldIds() == null) {
			throw new BusinessException(ErrorCode.BAD_REQUEST, "필수 파라미터가 누락되었습니다.");
		}

		// holdId 중복 체크
		Set<Integer> holdIdSet = new LinkedHashSet<>(payRequest.getHoldIds());
		if (holdIdSet.size() != payRequest.getHoldIds().size()) {
			throw new BusinessException(ErrorCode.BAD_REQUEST);
		}

		buyService.pay(payRequest, userNo);

		result.put("message", "success");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
}
