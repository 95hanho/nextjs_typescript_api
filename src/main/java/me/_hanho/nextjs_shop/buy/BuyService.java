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
	
	public List<Coupon> getAvailableCoupon(List<Integer> productIds) {
		return buyMapper.getAvailableCoupon(productIds);
	}
	
	public List<ProductWithCouponsDTO> getProductWithCoupons(List<BuyProduct> products, String user_id) {
		return buyMapper.getProductWithCoupons(products, user_id);
	}
	
	@Transactional
	public void pay(PayRequest payRequest) {
		// 할 일
		// nextjs_shop_order_group(주문프로세스) INSERT
		OrderGroup orderGroup = OrderGroup.builder().user_id(payRequest.getUser_id()).total_price(payRequest.getTotalFinalBeforeDelivery())
				.shipping_fee(payRequest.getDeliveryFee()).payment_method(payRequest.getPayment_method()).discount_price(payRequest.getTotalDiscount())
				.status("PAID").build();
		int order_id = buyMapper.insertOrderGroup(orderGroup);
		// nextjs_shop_order_list(주문목록) INSERT
		buyMapper.insertOrderList(payRequest.getItems(), order_id, payRequest.getUser_id());
		// nextjs_shop_stock_hold의 점유 status, active_hold 변경
		buyMapper.updateCancelStockHold(payRequest.getItems());
		// nextjs_shop_product_detail(상품상세옵션) stock(재고수), sales_count(판매수) 변경
		List<ProductWithCouponsDTO> productWithCouponList = payRequest.getItems();
		
		// test2
		// test3
		// test4
		
		// nextjs_shop_usercoupon(유저쿠폰) used(사용여부) 변경
		// nextjs_shop_coupon(쿠폰) amount(수량) 변경
	}

	public void updateProductDetailByBuy(ProductDetail productDetail) {
		// 재고 - 1, 판매량 + 1
		buyMapper.updateProductDetailByBuy(productDetail);
	}









}
