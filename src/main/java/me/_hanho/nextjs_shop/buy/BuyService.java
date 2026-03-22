package me._hanho.nextjs_shop.buy;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import me._hanho.nextjs_shop.common.exception.BusinessException;
import me._hanho.nextjs_shop.common.exception.ErrorCode;
import me._hanho.nextjs_shop.model.StockHoldCoupon;

@Service
@RequiredArgsConstructor
public class BuyService {
	
	// private static final int HOLD_TTL_SECONDS = 180; // 3분(연장1분마다 최소 2분 여유)
	private static final int HOLD_TTL_SECONDS = 60 * 60 * 24; // TEST 용 1일
	
	private final BuyMapper buyMapper;
	
    @lombok.Data
    public static class HoldTryResult {
        private boolean ok;
        private java.util.List<HoldBrief> holds;

        public static HoldTryResult ok(java.util.List<HoldBrief> holds) {
            var r = new HoldTryResult();
            r.ok = true;
            r.holds = holds;
            return r;
        }

        public static HoldTryResult fail() {
            var r = new HoldTryResult();
            r.ok = false;
            r.holds = java.util.List.of();
            return r;
        }
    }

    @Transactional
    public HoldTryResult preparePurchaseWithHold(BuyCheckRequest req, Integer userNo) {
        // 0. 요청 정리
        // productOptionId 기준으로 수량은 합산하되,
        // 같은 옵션의 cartId는 함께 관리한다.
        Map<Integer, Integer> mergedCountByPd = new HashMap<>();
        Map<Integer, Integer> cartIdByPd = new HashMap<>();

        for (var x : req.getBuyList()) {
            int pdId = x.getProductOptionId();
            int cnt = Math.max(1, x.getCount());
            Integer cartId = x.getCartId(); // 장바구니 구매면 값 존재, 바로구매면 null

            mergedCountByPd.merge(pdId, cnt, Integer::sum);

            // 같은 상품옵션이 여러 번 들어올 수는 있지만,
            // 현재 hold 구조는 productOptionId 기준 1건으로 관리하므로
            // 서로 다른 cartId가 섞이면 어느 cart를 대표로 저장할지 애매하다.
            // 따라서 같은 옵션에 서로 다른 cartId가 들어오면 예외 처리한다.
            if (cartIdByPd.containsKey(pdId)) {
                Integer existingCartId = cartIdByPd.get(pdId);

                if (!java.util.Objects.equals(existingCartId, cartId)) {
                    throw new BusinessException(ErrorCode.BAD_REQUEST);
                }
            } else {
                cartIdByPd.put(pdId, cartId);
            }
        }

        if (mergedCountByPd.isEmpty()) {
            return HoldTryResult.fail();
        }

        var requestedDetailIds = new ArrayList<>(mergedCountByPd.keySet());

        // 1. 유저의 현재 활성 HOLD 전체 조회
        List<Integer> allExistingHolds = buyMapper.selectAllActiveHoldsByUser(userNo);

        // 2. 기존 활성 HOLD 해제
        // 현재 구조상 구매 진입 시 기존 점유는 모두 정리하고 새 요청 기준으로 다시 맞춤
        List<Integer> toRelease = new ArrayList<>();
        for (Integer h : allExistingHolds) {
            toRelease.add(h);
        }

        if (!toRelease.isEmpty()) {
            buyMapper.releaseHolds(toRelease, userNo);
        }

        // 3. 가용 수량 체크
        var availableMap = buyMapper.selectAvailability(requestedDetailIds).stream()
            .collect(Collectors.toMap(
                AvailabilityRow::getProductOptionId,
                AvailabilityRow::getAvailable
            ));

        // 4. 현재 요청 옵션 기준 남아 있는 active hold 재조회
        var remainingHolds = buyMapper.selectExistingHolds(userNo, requestedDetailIds);

        // product_option_id -> 기존 hold count 합산
        Map<Integer, Integer> existingCountSum = new HashMap<>();
        for (var h : remainingHolds) {
            existingCountSum.merge(h.getProductOptionId(), h.getCount(), Integer::sum);
        }

        for (var entry : mergedCountByPd.entrySet()) {
            int pdId = entry.getKey();
            int reqCnt = entry.getValue();
            int existingCnt = existingCountSum.getOrDefault(pdId, 0);

            int needed = Math.max(0, reqCnt - existingCnt);
            int availableGlobal = availableMap.getOrDefault(pdId, 0);

            if (availableGlobal < needed) {
                return HoldTryResult.fail();
            }
        }

        // 5. hold upsert
        // productOptionId별로 cartId를 함께 넣어 저장한다.
        var upserts = new ArrayList<UpsertHoldRow>();
        for (var entry : mergedCountByPd.entrySet()) {
            Integer productOptionId = entry.getKey();
            Integer count = entry.getValue();
            Integer cartId = cartIdByPd.get(productOptionId);

            upserts.add(new UpsertHoldRow(
                userNo,
                productOptionId,
                cartId,
                count,
                HOLD_TTL_SECONDS,
                req.getReturnUrl()
            ));
        }
        buyMapper.upsertHolds(upserts);

        // 6. 최종 hold 조회
        var holds = buyMapper.selectLatestHolds(userNo, requestedDetailIds);

        // 7. 요청한 옵션이 전부 정상 점유되었는지 검증
        Map<Integer, Integer> heldCountMap = new HashMap<>();
        for (var h : holds) {
            heldCountMap.put(h.getProductOptionId(), h.getCount());
        }

        for (var e : mergedCountByPd.entrySet()) {
            int optionId = e.getKey();
            int expected = e.getValue();
            Integer actual = heldCountMap.get(optionId);

            if (actual == null || actual != expected) {
                throw new BusinessException(ErrorCode.STOCK_HOLD_UPSERT_INCOMPLETE);
            }
        }

        // ------------------------------------------------------------------
        // 8. 선택 쿠폰 다운로드 + hold_coupon 매핑 저장
        // ------------------------------------------------------------------

        // 8-1. buyList에서 선택된 couponId 전체 수집
        //      중복 제거를 위해 Set 사용
        Set<Integer> requestedCouponIds = new LinkedHashSet<>();
        for (var item : req.getBuyList()) {
            if (item.getCouponIds() == null || item.getCouponIds().isEmpty()) {
                continue;
            }

            for (Integer couponId : item.getCouponIds()) {
                if (couponId != null) {
                    requestedCouponIds.add(couponId);
                }
            }
        }

        // 쿠폰 선택이 있는 경우에만 처리
        if (!requestedCouponIds.isEmpty()) {

            // 8-2. 선택된 쿠폰을 유저 쿠폰 테이블에 다운로드
            //      이미 받은 쿠폰은 INSERT IGNORE / NOT EXISTS 등으로 중복 방지
            buyMapper.downloadCoupons(new ArrayList<>(requestedCouponIds), userNo);

            // 8-3. 방금 선택한 couponId들에 대해
            //      user_coupon_id 매핑 정보 조회
            //      (coupon_id -> user_coupon_id)
            List<UserCouponRow> downloadedUserCoupons =
                buyMapper.selectUserCouponsByCouponIds(userNo, new ArrayList<>(requestedCouponIds));

            Map<Integer, Integer> userCouponIdByCouponId = new HashMap<>();
            for (var uc : downloadedUserCoupons) {
                userCouponIdByCouponId.put(uc.getCouponId(), uc.getUserCouponId());
            }

            // 8-4. 점유된 hold를 product_option_id 기준으로 매핑
            //      (product_option_id -> hold_id)
            Map<Integer, Integer> holdIdByProductOptionId = new HashMap<>();
            for (var hold : holds) {
                holdIdByProductOptionId.put(hold.getProductOptionId(), hold.getHoldId());
            }

            // 8-5. 기존 hold_coupon 매핑 삭제
            //      같은 hold에 대해 구매페이지 재진입/재선택 시 중복 저장 방지
            List<Integer> holdIds = holds.stream()
                .map(HoldBrief::getHoldId)
                .collect(Collectors.toList());

            if (!holdIds.isEmpty()) {
                buyMapper.deleteHoldCouponsByHoldIds(holdIds);
            }

            // 8-6. 상품별 선택 쿠폰을 hold_id + user_coupon_id 형태로 변환
            //      예: 상품옵션 A -> [쿠폰1, 쿠폰2]
            //          => hold_id(A) + user_coupon_id(쿠폰1), hold_id(A) + user_coupon_id(쿠폰2)
            List<StockHoldCoupon> holdCouponRows = new ArrayList<>();

            for (var item : req.getBuyList()) {
                Integer holdId = holdIdByProductOptionId.get(item.getProductOptionId());

                // hold 매핑이 없으면 비정상 상태
                if (holdId == null) {
                    throw new BusinessException(ErrorCode.COUPON_APPLY_FAILED);
                }

                if (item.getCouponIds() == null || item.getCouponIds().isEmpty()) {
                    continue;
                }

                for (Integer couponId : item.getCouponIds()) {
                    if (couponId == null) {
                        continue;
                    }

                    Integer userCouponId = userCouponIdByCouponId.get(couponId);

                    // 다운로드 후에도 user_coupon_id를 못 찾으면 비정상
                    if (userCouponId == null) {
                        throw new BusinessException(ErrorCode.COUPON_APPLY_FAILED);
                    }

                    holdCouponRows.add(new StockHoldCoupon(
                        0,
                        holdId,
                        userCouponId
                    ));
                }
            }

            // 8-7. hold_coupon 테이블 저장
            if (!holdCouponRows.isEmpty()) {
                int inserted = buyMapper.applyCoupons(holdCouponRows);

                if (inserted != holdCouponRows.size()) {
                    throw new BusinessException(ErrorCode.COUPON_APPLY_FAILED);
                }
            }
        }

        return HoldTryResult.ok(holds);
    }
    /* -------------------------------------------------------------------------------- */

    // public static record Item(int pdId, int count) {}
    
	public List<Integer> selectAllActiveHoldsByUser(Integer userNo) {
		return buyMapper.selectAllActiveHoldsByUser(userNo);
	}
    
    public int extendHolds(List<Integer> holdIds, Integer userNo) {
        if (holdIds == null || holdIds.isEmpty()) return 0;
        // HOLD 상태(활성)만 NOW()+TTL로 연장
        return buyMapper.extendHolds(holdIds, userNo, HOLD_TTL_SECONDS);
    }
    // 점유해제
    public int releaseHolds(List<Integer> holdIds, Integer userNo) {
        if (holdIds == null || holdIds.isEmpty()) return 0;
        // HOLD 상태(활성) → RELEASED, activeHold=NULL
        return buyMapper.releaseHolds(holdIds, userNo);
    }

    public void addHoldCoupon(Integer holdId, Integer userCouponId, Integer userNo) {
        buyMapper.addHoldCoupon(holdId, userCouponId, userNo);
    }

    public void removeHoldCoupon(Integer holdCouponId, Integer userNo) {
        buyMapper.removeHoldCoupon(holdCouponId, userNo);
    }
    
	public List<OrderStockResponse> getStockHoldProductList(Integer userNo) {
		return buyMapper.getStockHoldProductList(userNo);
	}

    
    public List<AvailableCartCouponAtBuyResponse> getAvailableCartCouponsAtBuy(List<Integer> productIds, Integer userNo) {
		return buyMapper.getAvailableCartCouponsAtBuy(productIds, userNo);
	}
	
	public List<AvailableSellerCouponAtBuyResponse> getAvailableSellerCouponsAtBuy(List<Integer> productIds, Integer userNo) {
		return buyMapper.getAvailableSellerCouponsAtBuy(productIds, userNo);
	}

    public List<StockHoldCoupon> getInitialHoldCoupons(List<Integer> holdIds) {
        return buyMapper.selectHoldCouponsByHoldIds(holdIds);
    }

    public DefaultAddressResponse getDefaultAddress(Integer userNo) {
        List<DefaultAddressResponse> addresses = buyMapper.getDefaultAddress(userNo);
        if (addresses == null || addresses.isEmpty()) {
            return null; // 기본 배송지가 없는 경우 null 반환
        }
        return addresses.get(0); // 기본 배송지는 하나라고 가정하고 첫 번째 항목 반환
    }
	
	public List<ProductWithCouponResponse> getProductWithCoupons(List<BuyProduct> products, Integer userNo) {
		return buyMapper.getProductWithCoupons(products, userNo);
	}
	
	@Transactional
	public void pay(PayRequest payRequest, Integer userNo) {
		// nextjs_shop_user UPDATE 마일리지 사용한거 없애고, 빠진 마일리지 조회
		buyMapper.updateUserMileageByBuy(payRequest);
		int remainingMileage = buyMapper.getUserMileage(userNo);
		
		// nextjs_shop_order_group(주문프로세스) INSERT
		BuyOrderGroup orderGroup = BuyOrderGroup.builder().userNo(userNo).eachCouponDiscountTotal(payRequest.getEachCouponDiscountTotal())
				.commonCouponDiscountTotal(payRequest.getCommonCouponDiscountTotal()).shippingFee(BigDecimal.valueOf(payRequest.getShippingFee()))
				.usedMileage(payRequest.getUsedMileage()).remainingMileage(remainingMileage)
				.totalPrice(payRequest.getTotalFinal()).paymentMethod(payRequest.getPaymentMethod())
				.userCouponId(payRequest.getUserCouponId()).addressId(payRequest.getAddressId())
				.paymentCode("0000").status("PAID").build();
		buyMapper.insertOrderGroup(orderGroup);
		int orderId = buyMapper.getOrderId(userNo);
		// 
		List<ProductWithCouponResponse> productWithCouponList = payRequest.getItems();
		// nextjs_shop_order_item(주문목록) INSERT
		buyMapper.insertOrderList(productWithCouponList, orderId, userNo);
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
