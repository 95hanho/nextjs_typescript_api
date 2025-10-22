package me._hanho.nextjs_shop.buy;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import me._hanho.nextjs_shop.model.Coupon;
import me._hanho.nextjs_shop.model.OrderGroup;
import me._hanho.nextjs_shop.model.ProductDetail;
import me._hanho.nextjs_shop.model.User;

@Service
public class BuyService {
	
	private static final int HOLD_TTL_SECONDS = 180; // 3분(연장1분마다 최소 2분 여유)
	@Autowired
	private BuyMapper buyMapper;
	
	
    @Transactional
    public HoldTryResult tryHoldUpsertAllOrNothing(BuyCheckRequest req) {
        // 요청 정리
        var items = req.getBuyList().stream()
            .map(x -> new Item(x.getProduct_detail_id(), Math.max(1, x.getCount())))
            .toList();
        if (items.isEmpty()) return HoldTryResult.fail();

        var detailIds = items.stream().map(Item::pdId).toList();

        // 1) 전역 가용수량 (stock - 모든 유저 미만료 HOLD 합)
        var availableMap = buyMapper.selectAvailability(detailIds).stream()
            .collect(java.util.stream.Collectors.toMap(
                AvailabilityRow::getProductDetailId, AvailabilityRow::getAvailable));

        // 2) 본인 기존 HOLD (미만료) 조회
        var existingMap = buyMapper.selectExistingHolds(req.getUser_id(), detailIds).stream()
            .collect(java.util.stream.Collectors.toMap(
                ExistingHold::getProductDetailId, e -> e));

        // 3) needed 계산 및 전부 가능 여부 확인
        for (var it : items) {
            int pdId = it.pdId();
            int reqCnt = it.count();
            int existingCnt = existingMap.getOrDefault(pdId, ExistingHold.ZERO).getCount();
            int needed = Math.max(0, reqCnt - existingCnt);
            int availableGlobal = availableMap.getOrDefault(pdId, 0);

            if (availableGlobal < needed) {
                return HoldTryResult.fail();
            }
        }

        // 4) 전부 가능 → 업서트(본인 것은 덮어쓰기/연장, 없으면 생성)
        //    ON DUPLICATE KEY (UQ_user_pd_status) 로 멱등 업서트
        var upserts = new java.util.ArrayList<UpsertHoldRow>();
        for (var it : items) {
            upserts.add(new UpsertHoldRow(req.getUser_id(), it.pdId(), it.count(), HOLD_TTL_SECONDS));
        }
        buyMapper.upsertHolds(upserts); // count=요청수량으로 덮어쓰기 + expires_at 연장

        // 5) 최종 hold_id 반환(하트비트용)
        var holds = buyMapper.selectLatestHolds(req.getUser_id(), detailIds);
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
    
    public int extendHolds(List<Integer> holdIds) {
        if (holdIds == null || holdIds.isEmpty()) return 0;
        // HOLD 상태(활성)만 NOW()+TTL로 연장
        return buyMapper.extendHolds(holdIds, HOLD_TTL_SECONDS);
    }

    public int releaseHolds(List<Integer> holdIds) {
        if (holdIds == null || holdIds.isEmpty()) return 0;
        // HOLD 상태(활성) → RELEASED, active_hold=NULL
        return buyMapper.releaseHolds(holdIds);
    }
    
	public List<OrderStockDTO> getOrderStock(String user_id) {
		List<OrderStockDTO> orderStock = buyMapper.getOrderStock(user_id);
		return orderStock;
	}
	
	public List<AvailableCoupon> getAvailableCoupon(List<Integer> productIds, String user_id) {
		return buyMapper.getAvailableCoupon(productIds, user_id);
	}
	
	public List<ProductWithCouponsDTO> getProductWithCoupons(List<BuyProduct> products, String user_id) {
		return buyMapper.getProductWithCoupons(products, user_id);
	}
	
	@Transactional
	public void pay(PayRequest payRequest) {
		
		// nextjs_shop_user UPDATE 마일리지 사용한거 없애고, 빠진 마일리지 조회
		buyMapper.updateUserMileageByBuy(payRequest);
		int remaining_mileage = buyMapper.getUserMileage(payRequest.getUser_id());
		
		System.out.println("remaining_mileage : " + remaining_mileage);
		
		// nextjs_shop_order_group(주문프로세스) INSERT
		OrderGroup orderGroup = OrderGroup.builder().user_id(payRequest.getUser_id()).eachcoupon_discount_total(payRequest.getEachcoupon_discount_total())
				.commoncoupon_discount_total(payRequest.getCommoncoupon_discount_total()).shipping_fee(payRequest.getShipping_fee())
				.used_mileage(payRequest.getUsed_Mileage()).remaining_mileage(remaining_mileage)
				.total_price(payRequest.getTotalFinal()).payment_method(payRequest.getPayment_method())
				.usercoupon_id(payRequest.getUsercoupon_id()).address_id(payRequest.getAddress_id())
				.payment_code("0000").status("PAID").build();
		buyMapper.insertOrderGroup(orderGroup);
		int order_id = buyMapper.getOrderId(payRequest.getUser_id());
		// 
		List<ProductWithCouponsDTO> productWithCouponList = payRequest.getItems();
		// nextjs_shop_order_list(주문목록) INSERT
		buyMapper.insertOrderList(productWithCouponList, order_id, payRequest.getUser_id());
		/*
		// nextjs_shop_stock_hold의 점유 status, active_hold 변경
		buyMapper.updateCancelStockHold(productWithCouponList);
		// nextjs_shop_product_detail(상품상세옵션) stock(재고수), sales_count(판매수) 변경
		buyMapper.updateProductDetailByBuy(productWithCouponList);
		// nextjs_shop_usercoupon(유저쿠폰) used(사용여부) 변경
		buyMapper.updateUserCouponUsed(productWithCouponList, payRequest.getUsercoupon_id());
		// nextjs_shop_coupon(쿠폰) amount(수량) 변경
		buyMapper.updateCommonCouponByBuy(payRequest.getUsercoupon_id());
		buyMapper.updateEachCouponByBuy(productWithCouponList);
		*/
	}

}
