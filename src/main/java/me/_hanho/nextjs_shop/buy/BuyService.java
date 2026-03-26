package me._hanho.nextjs_shop.buy;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.catalina.servlets.DefaultServlet.SortManager.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import me._hanho.nextjs_shop.common.exception.BusinessException;
import me._hanho.nextjs_shop.common.exception.ErrorCode;
import me._hanho.nextjs_shop.model.OrderGroup;
import me._hanho.nextjs_shop.model.OrderItem;
import me._hanho.nextjs_shop.model.OrderItemCoupon;
import me._hanho.nextjs_shop.model.StockHoldCoupon;
import me._hanho.nextjs_shop.util.OrderCodeGenerator;

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

    public void addStockHoldCoupons(List<ManageStockHoldCoupon> holdCouponRequests) {
        buyMapper.addStockHoldCoupons(holdCouponRequests);
    }

    public void deleteStockHoldCoupons(List<ManageStockHoldCoupon> holdCouponRequests) {
        buyMapper.deleteStockHoldCoupons(holdCouponRequests);
    }
    
	public List<OrderStockResponse> getStockHoldProductList(Integer userNo) {
		return buyMapper.getStockHoldProductList(userNo);
	}

    public List<LatestHoldInfo> getLatestHoldsInfo(Integer userNo) {
        LocalDateTime latestCreatedAt = buyMapper.getMaxCreatedAtStockHold(userNo);

        return buyMapper.getLatestHoldsInfo(userNo, latestCreatedAt);
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
	
	@Transactional
	public void pay(PayRequest payRequest, Integer userNo) {
        List<Integer> holdIds = payRequest.getHoldIds();
        // [Mapper] : 가장 최근의 HOLD된 제품 정보 가져오기(조회겸 인증, 재고, 판매자 상태, 제품 상태 등 모두 검사)
        List<OrderStockResponse> stockHoldProductList = buyMapper.getStockHoldProductListByHoldIds(holdIds, userNo);
        // Check : request의 holdId와 일치하는지 검증(집합 비교)
        if(stockHoldProductList.size() != holdIds.size()) {
            // > ERROR : 일치하지 한꺼번에 않으면 예외 처리("구매과정 중 오류가 발생했습니다.")
            throw new BusinessException(ErrorCode.NOT_MATCHED_HOLD);
        }
        // [Mapper] : 적용된 쿠폰 갯수 조회(stock_hold_coupon 테이블에서 holdId 기준으로 user_coupon_id 개수 조회)
        int appliedCouponCount = buyMapper.selectHoldCouponsByHoldIds(holdIds).size();
        // [Mapper] : 쿠폰가져오기 (holdId 기준, stock_hold_coupon에서 실제 사용할 수 있는 쿠폰)
        List<PayAvailableCoupon> availableCoupons = buyMapper.getAvailableCouponsByHoldIds(holdIds);
        // Check : 쿠폰 갯수 비교 (점유에 적용된 쿠폰 갯수와 실제 적용할 수 있는 쿠폰 갯수 비교)
        if(appliedCouponCount != availableCoupons.size()) {
            // > ERROR : 일치하지 않으면 예외 처리("쿠폰 적용 과정 중 오류가 발생했습니다.")
            throw new BusinessException(ErrorCode.COUPON_UNAVAILABLE_FAILED);
        }

        // ----------- 결제 진행 -----------

        // 결제 금액 계산
        BigDecimal sellerCouponDiscountTotal = BigDecimal.ZERO; // 판매자 쿠폰 할인 총액
        BigDecimal cartCouponDiscountTotal = BigDecimal.ZERO; // 공용 쿠폰 할인 총액
        BigDecimal shippingFee = BigDecimal.ZERO; // 배송비
        BigDecimal totalPrice = BigDecimal.ZERO; // 최종 결제 금액
        OrderGroup orderGroup; // 주문 그룹 정보
        List<OrderItem> orderItems = new ArrayList<>(); // 주문 아이템 목록
        List<OrderItemCoupon> orderItemCoupons = new ArrayList<>(); // 주문 아이템 쿠폰 목록

        // holdId별 쿠폰
        Map<Integer, List<PayAvailableCoupon>> couponMap =
                availableCoupons.stream()
                    .collect(Collectors.groupingBy(PayAvailableCoupon::getHoldId));
        // 배송비 계산을 위한 맵
        Map<String, DeliveryInfoBySeller> deliveryMap = new HashMap<>();

        for (OrderStockResponse sh : stockHoldProductList) {
             // 할인적용 전 가격
            int priceBeforeCoupon = (sh.getFinalPrice() + sh.getAddPrice()) * sh.getCount();

            // ---- 배송비 계산을 위한 저장 ----
            if(!deliveryMap.containsKey(sh.getSellerName())) {
                DeliveryInfoBySeller info = new DeliveryInfoBySeller();
                info.setTotalFinalPrice(priceBeforeCoupon);
                info.setBaseShippingFee(sh.getBaseShippingFee());
                info.setFreeShippingMinAmount(appliedCouponCount);
                deliveryMap.put(sh.getSellerName(), info);
            } else {
                DeliveryInfoBySeller info = deliveryMap.get(sh.getSellerName());
                info.setTotalFinalPrice(info.getTotalFinalPrice() + priceBeforeCoupon);
            }

            // ---- 쿠폰 적용 로직 ---
            boolean useStack = false; // 중복 쿠폰 사용 여부(사용했는데 또 사용하면 오류)
            // 해당 점유의 쿠폰
            List<PayAvailableCoupon> matchedCoupons =
                    couponMap.getOrDefault(sh.getHoldId(), Collections.emptyList());
           
            for (PayAvailableCoupon c : matchedCoupons) {
                // ---- 쿠폰 중복 사용 여부 체크
                if (c.getIsStackable()) {
                    useStack = true;
                } else if (!c.getIsStackable() && useStack) {
                    // > ERROR : 스택 불가능한 쿠폰이 이미 스택 가능한 쿠폰과 함께 사용된 경우 예외 처리("쿠폰 적용 과정 중 오류가 발생했습니다.")
                    throw new BusinessException(ErrorCode.COUPON_UNAVAILABLE_FAILED);
                }
                // ---- 쿠폰 적용가 계산
                int discountAmount = 0;
                if(priceBeforeCoupon < c.getMinimumOrderBeforeAmount().intValue()) {
                    // > ERROR : 쿠폰의 최소 주문 금액 조건 미충족 시 예외 처리("쿠폰 적용 과정 중 오류가 발생했습니다.")
                    throw new BusinessException(ErrorCode.COUPON_UNAVAILABLE_FAILED);
                }
                if(c.getDiscountType().equals("fixed_amount")) {
                    // 정액 할인인 경우 단순히 할인 금액만큼 차감
                    discountAmount = c.getDiscountValue().intValue();
                } else if(c.getDiscountType().equals("percentage")) {
                    // 비율 할인인 경우 가격에 비율만큼 차감
                    discountAmount = (int) Math.floor(priceBeforeCoupon * c.getDiscountValue().doubleValue() / 100);
                    if(discountAmount > c.getMaxDiscount().intValue()) {
                        discountAmount = c.getMaxDiscount().intValue();
                    }
                }
                // 장바구니 쿠폰
                if(c.getSellerName() == null) {
                    cartCouponDiscountTotal = cartCouponDiscountTotal.add(BigDecimal.valueOf(discountAmount));
                } 
                // 판매자 쿠폰
                else {
                    sellerCouponDiscountTotal = sellerCouponDiscountTotal.add(BigDecimal.valueOf(discountAmount));
                }
                priceBeforeCoupon -= discountAmount;
                // 주문 아이템 쿠폰 정보 저장
                OrderItemCoupon orderItemCoupon = OrderItemCoupon.builder()
                    .userCouponId(c.getUserCouponId())
                    .discountedPrice(BigDecimal.valueOf(discountAmount))
                    .couponId(c.getCouponId())
                    .description(c.getDescription())
                    // .couponCode() // 만드는 서비스 메소드 만들어서 붙여주기
                    .discountType(c.getDiscountType())
                    .discountValue(c.getDiscountValue())
                    .maxDiscount(c.getMaxDiscount())
                    .minimumOrderBeforeAmount(c.getMinimumOrderBeforeAmount())
                    .build();
                orderItemCoupons.add(orderItemCoupon);
            }
            //
            totalPrice = totalPrice.add(BigDecimal.valueOf(priceBeforeCoupon));
            OrderItem orderItem = OrderItem.builder()
                .holdId(sh.getHoldId())
                .productName(sh.getProductName())
                .count(sh.getCount())
                .size(sh.getSize())
                .originPrice(sh.getOriginPrice())
                .finalPrice(sh.getFinalPrice())
                .addPrice(sh.getAddPrice()) // 옵션 추가 금액 있으면 계산해서 넣어주기
                .couponDiscountedPrice(BigDecimal.valueOf((sh.getFinalPrice() + sh.getAddPrice()) * sh.getCount() - priceBeforeCoupon)) // 만드는 서비스 메소드 만들어서 붙여주기
                .totalPrice(BigDecimal.valueOf(priceBeforeCoupon))
                .build();
            orderItems.add(orderItem);
        }

        // 배송비 계산 shippingFee
        deliveryMap.forEach((sellerName, info) -> {
            // 배송비 계산 : 총 결제 금액이 배송비 무료 기준 미만이면 배송비 추가
            if(info.getTotalFinalPrice() < info.getFreeShippingMinAmount()) {
                shippingFee.add(BigDecimal.valueOf(info.getBaseShippingFee()));
            }
        });

        // 주소 id number | null
        Integer addressId = payRequest.getShippingAddress().getAddressId();

        // [Mapper] : 신규주소일 시 주소 추가
        if(payRequest.getShippingAddress() != null && payRequest.getShippingAddress().getAddressId() == null) {
            buyMapper.insertUserAddress(payRequest.getShippingAddress(), payRequest.getSetAsDefault() , userNo);
            addressId = buyMapper.getLatestAddressIdByUserNo(userNo); // 방금 추가한 주소의 ID 조회
        }

        // [Mapper] : 신규주소가 아니고, '기본주소로 설정이면' 기본주소를 해당 주소로 변경
        if(payRequest.getShippingAddress() != null && payRequest.getShippingAddress().getAddressId() != null && payRequest.getSetAsDefault()) {
            // 원래 기본주소 취소
            buyMapper.updateUserAddressCancelDefault(userNo);
            // 선택한 주소를 기본주소로 변경
            buyMapper.updateUserAddressDefault(payRequest.getShippingAddress().getAddressId(), userNo);
        }

        // [Mapper] :유저의 마일리지 조회
        int hasMileage = buyMapper.getUserMileage(userNo);
        // Check : 사용 마일리지 검증(보유 마일리지보다 사용 마일리지가 많으면 예외 처리("마일리지 사용 과정 중 오류가 발생했습니다."))
        if(payRequest.getUsedMileage() > hasMileage) {
            throw new BusinessException(ErrorCode.MILEAGE_UNAVAILABLE_FAILED);
        }

        // 주문프로세스 만들기
        // 결제코드 가상 생성
        String payCode = OrderCodeGenerator.generatePayCode();
        orderGroup = OrderGroup.builder()
            .userNo(userNo)
            .sellerCouponDiscountTotal(sellerCouponDiscountTotal)
            .cartCouponDiscountTotal(cartCouponDiscountTotal)
            .shippingFee(shippingFee)
            .usedMileage(payRequest.getUsedMileage())
            .remainingMileage(hasMileage - payRequest.getUsedMileage())
            .totalPrice(totalPrice)
            .paymentMethod(payRequest.getPaymentMethod())
            .paymentCode(payCode)
            .status("ORDERED")
            .addressId(addressId)
            .addressName(payRequest.getShippingAddress().getAddressName())
            .recipientName(payRequest.getShippingAddress().getRecipientName())
            .addressPhone(payRequest.getShippingAddress().getAddressPhone())
            .zonecode(payRequest.getShippingAddress().getZonecode())
            .address(payRequest.getShippingAddress().getAddress())
            .addressDetail(payRequest.getShippingAddress().getAddressDetail())
            .memo(payRequest.getShippingAddress().getMemo())
            .build();

        // [Mapper] : 주문프로세스 INSERT(order_group)
        buyMapper.insertOrderGroup(orderGroup);
        int order_id = buyMapper.getOrderId(userNo); // 방금 생성된 주문 그룹 ID 조회

        // 주문목록 만들기

        // [Mapper] : 주문목록 INSERT(order_item)

        // 주문목록 쿠폰 만들기

        // [Mapper] : 주문목록 쿠폰(order_item_coupon) INSERT

        // [Mapper] : product_option stock, sales_count 수량 변경
        /*
        // 조건부 차감하기 row count 0 이면 실패처리
            UPDATE product_option
            SET stock = stock - #{count},
                sales_count = sales_count + #{count}
            WHERE product_option_id = #{productOptionId}
            AND stock >= #{count}
        */

        // [Mapper] : user_coupon 사용 처리
        // [Mapper] : coupon amount 차감
        // [Mapper] : user mileage 차감, 적립 처리(조건부 업데이트, 보유 마일리지 부족 시 실패 처리)

        // [Mapper] : stock_hold status를 PAID로 변경(row count 검증)

        // [Mapper] : 장바구니 정보 있으면 장바구니 삭제

        // [Mapper] : user_address usedate_at 최신화
	}

}
