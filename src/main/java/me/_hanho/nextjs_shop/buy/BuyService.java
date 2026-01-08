package me._hanho.nextjs_shop.buy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import me._hanho.nextjs_shop.model.OrderGroup;
import me._hanho.nextjs_shop.model.StockHold;

@Service
public class BuyService {
	
	private static final int HOLD_TTL_SECONDS = 180; // 3분(연장1분마다 최소 2분 여유)
	@Autowired
	private BuyMapper buyMapper;
	
	
    @Transactional
    public HoldTryResult tryHoldUpsertAllOrNothing(BuyCheckRequest req) {
    	// 0. 요청 정리
        Map<Integer, Integer> mergedCountByPd = new HashMap<>();
        for (var x : req.getBuyList()) {
            int pdId = x.getProductOptionId();
            int cnt  = Math.max(1, x.getCount());
            mergedCountByPd.merge(pdId, cnt, Integer::sum);
        }
        if (mergedCountByPd.isEmpty()) return HoldTryResult.fail();

        var requestedDetailIds = new ArrayList<>(mergedCountByPd.keySet());

        // 1. 유저의 모든 현재 HOLD(미만료) 다 가져오기 (※ 여기서는 detailIds 필터 걸지 말고 전체)
        List<StockHold> allExistingHolds = buyMapper.selectAllActiveHoldsByUser(req.getUserId());
        // ↑ 새로 하나 만들어야 돼. selectExistingHolds(userId, detailIds) 말고.

        // 2. 이번 요청에 포함되지 않은 hold 들은 RELEASE
        List<Integer> toRelease = new ArrayList<>();
        for (var h : allExistingHolds) {
            toRelease.add(h.getHoldId());
        }
        if (!toRelease.isEmpty()) {
            buyMapper.releaseHolds(toRelease, req.getUserId()); // status='RELEASED', active_hold=NULL
        }

        // 3. 가용수량 체크 (지금 있는 로직과 유사)
        var availableMap = buyMapper.selectAvailability(requestedDetailIds).stream()
            .collect(Collectors.toMap(
                AvailabilityRow::getProductOptionId,
                AvailabilityRow::getAvailable
            ));

        // 요청된 detailId 중 아직 남아 있는 active HOLD만 다시 조회
        var remainingHolds = buyMapper.selectExistingHolds(
            req.getUserId(),
            requestedDetailIds
        );

        // product_option_id -> count 합산
        Map<Integer, Integer> existingCountSum = new HashMap<>();
        for (var h : remainingHolds) {
            existingCountSum.merge(h.getProductOptionId(), h.getCount(), Integer::sum);
        }

        for (var entry : mergedCountByPd.entrySet()) {
            int pdId   = entry.getKey();
            int reqCnt = entry.getValue();
            int existingCnt = existingCountSum.getOrDefault(pdId, 0);

            int needed = Math.max(0, reqCnt - existingCnt);
            int availableGlobal = availableMap.getOrDefault(pdId, 0);
            if (availableGlobal < needed) {
                return HoldTryResult.fail();
            }
        }

        // 4. 가능하면 upsert (요청된 detailId들만)
        var upserts = new ArrayList<UpsertHoldRow>();
        for (var entry : mergedCountByPd.entrySet()) {
            upserts.add(new UpsertHoldRow(
                req.getUserId(),
                entry.getKey(),
                entry.getValue(), // 최종 수량
                HOLD_TTL_SECONDS
            ));
        }
        buyMapper.upsertHolds(upserts);

        // 5. 최종 결과 반환
        var holds = buyMapper.selectLatestHolds(req.getUserId(), requestedDetailIds);
        return HoldTryResult.ok(holds);
    }

    public static record Item(int pdId, int count) {}

    @lombok.Data
    public static class HoldTryResult {
        private boolean ok;
        private java.util.List<HoldBrief> holds;
        public static HoldTryResult ok(java.util.List<HoldBrief> holds) {
            var r = new HoldTryResult(); r.ok = true; r.holds = holds; return r;
        }
        public static HoldTryResult fail() {
            var r = new HoldTryResult(); r.ok = false; r.holds = java.util.List.of(); return r;
        }
    }
    
    public int extendHolds(List<Integer> holdIds, String userId) {
        if (holdIds == null || holdIds.isEmpty()) return 0;
        // HOLD 상태(활성)만 NOW()+TTL로 연장
        return buyMapper.extendHolds(holdIds, userId, HOLD_TTL_SECONDS);
    }
    // 점유해제
    public int releaseHolds(List<Integer> holdIds, String userId) {
        if (holdIds == null || holdIds.isEmpty()) return 0;
        // HOLD 상태(활성) → RELEASED, activeHold=NULL
        return buyMapper.releaseHolds(holdIds, userId);
    }
    
	public List<OrderStockDTO> getOrderStock(String userId) {
		List<OrderStockDTO> orderStock = buyMapper.getOrderStock(userId);
		return orderStock;
	}
	
	public List<AvailableCoupon> getAvailableCoupon(List<Integer> productIds, String userId) {
		return buyMapper.getAvailableCoupon(productIds, userId);
	}
	
	public List<ProductWithCouponsDTO> getProductWithCoupons(List<BuyProduct> products, String userId) {
		return buyMapper.getProductWithCoupons(products, userId);
	}
	
	@Transactional
	public void pay(PayRequest payRequest) {
		// nextjs_shop_user UPDATE 마일리지 사용한거 없애고, 빠진 마일리지 조회
		buyMapper.updateUserMileageByBuy(payRequest);
		int remainingMileage = buyMapper.getUserMileage(payRequest.getUserId());
		
		// nextjs_shop_order_group(주문프로세스) INSERT
		OrderGroup orderGroup = OrderGroup.builder().userId(payRequest.getUserId()).eachCouponDiscountTotal(payRequest.getEachCouponDiscountTotal())
				.commonCouponDiscountTotal(payRequest.getCommonCouponDiscountTotal()).shippingFee(payRequest.getShippingFee())
				.usedMileage(payRequest.getUsedMileage()).remainingMileage(remainingMileage)
				.totalPrice(payRequest.getTotalFinal()).paymentMethod(payRequest.getPaymentMethod())
				.userCouponId(payRequest.getUserCouponId()).addressId(payRequest.getAddressId())
				.paymentCode("0000").status("PAID").build();
		buyMapper.insertOrderGroup(orderGroup);
		int orderId = buyMapper.getOrderId(payRequest.getUserId());
		// 
		List<ProductWithCouponsDTO> productWithCouponList = payRequest.getItems();
		// nextjs_shop_order_item(주문목록) INSERT
		buyMapper.insertOrderList(productWithCouponList, orderId, payRequest.getUserId());
		// nextjs_shop_stock_hold의 점유 status, active_hold 변경
		buyMapper.updateCancelStockHold(productWithCouponList);
		// nextjs_shop_product_option(상품상세옵션) stock(재고수), sales_count(판매수) 변경
		buyMapper.updateProductOptionByBuy(productWithCouponList);
		// nextjs_shop_user_coupon(유저쿠폰) used(사용여부) 변경
		buyMapper.updateUserCouponUsed(productWithCouponList, payRequest.getUserCouponId());
		// nextjs_shop_coupon(쿠폰) amount(수량) 변경
		buyMapper.updateCommonCouponByBuy(payRequest.getUserCouponId());
		buyMapper.updateEachCouponByBuy(productWithCouponList);
	}

}
