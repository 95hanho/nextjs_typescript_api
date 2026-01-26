package me._hanho.nextjs_shop.buy;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.List;

// 할인 가격 계산을 위한
public class PriceCalculatorService {

    private static final BigDecimal HUNDRED = new BigDecimal("100");
    private static final BigDecimal ZERO = BigDecimal.ZERO;
    private static final RoundingMode KRW_ROUND = RoundingMode.HALF_UP;
    private static final int KRW_SCALE = 0;
    
    public static void applyDiscounts(List<ProductWithCouponsDTO> items) {
        if (items == null || items.isEmpty()) return;
        items.forEach(PriceCalculatorService::applyDiscountToOne);
    }

    public static void applyDiscountToOne(ProductWithCouponsDTO dto) {
        // null-safe 값 준비
        BigDecimal minOrder = nvl(dto.getMinimumOrderBeforeAmount(), ZERO).setScale(KRW_SCALE, KRW_ROUND);
        BigDecimal discountValue = nvl(dto.getDiscountValue(), ZERO);
        String discountType = safe(dto.getDiscountType());

        int price = dto.getFinalPrice() == 0 ?  dto.getOriginPrice() : dto.getFinalPrice();
        
        // 소계 계산: (price + add_price) * count
        BigDecimal unitBase = bd(price).add(bd(dto.getAddPrice())).setScale(KRW_SCALE, KRW_ROUND);
        BigDecimal subtotalBefore = unitBase.multiply(new BigDecimal(dto.getCount())).setScale(KRW_SCALE, KRW_ROUND);

        boolean eligible = subtotalBefore.compareTo(minOrder) >= 0;

        BigDecimal discount = ZERO;
        if (eligible) {
            if ("percentage".equalsIgnoreCase(discountType)) {
                // 퍼센트 할인
                BigDecimal raw = subtotalBefore
                        .multiply(discountValue)
                        .divide(HUNDRED, KRW_SCALE, KRW_ROUND);

                // 퍼센트 타입에만 max_discount 적용 (null이면 상한 없음)
                BigDecimal maxDiscount = dto.getMaxDiscount();
                if (maxDiscount != null) {
                    maxDiscount = maxDiscount.setScale(KRW_SCALE, KRW_ROUND);
                    raw = raw.min(maxDiscount);
                }
                discount = raw;
            } else if ("fixed_amount".equalsIgnoreCase(discountType)) {
                // 고정액 할인: max_discount 무시 (null 가정)
                discount = discountValue.setScale(KRW_SCALE, KRW_ROUND);
            }
        }

        // 음수 방지 + 소계 초과 방지
        if (discount.compareTo(ZERO) < 0) discount = ZERO;
        if (discount.compareTo(subtotalBefore) > 0) discount = subtotalBefore;

        BigDecimal finalPrice = subtotalBefore.subtract(discount).setScale(KRW_SCALE, KRW_ROUND);

        dto.setDiscountAmount(discount);
        dto.setResultPrice(finalPrice);
    }

    /**
     * 공용(메인) 쿠폰을 합계 금액(base)에 적용하여 "할인액"을 반환.
     * - percentage: max_discount 상한 적용
     * - fixed_amount: 그대로 차감
     * - minimum_order_before_amount 이상일 때만 적용
     * - 음수/초과 방지
     */
    public static BigDecimal calcCommonCouponDiscount(BigDecimal base, AvailableCoupon coupon) {
        if (base == null) base = ZERO;
        base = base.setScale(KRW_SCALE, KRW_ROUND);
        
        System.out.println("coupon : " + coupon);

        if (!isUsableCommonCoupon(coupon)) return ZERO;

        BigDecimal minOrder = nvl(coupon.getMinimumOrderBeforeAmount(), ZERO).setScale(KRW_SCALE, KRW_ROUND);
        if (base.compareTo(minOrder) < 0) return ZERO;

        String discountType = safe(coupon.getDiscountType());
        BigDecimal discountValue = nvl(coupon.getDiscountValue(), ZERO);

        BigDecimal discount = ZERO;
        if ("percentage".equalsIgnoreCase(discountType)) {
            BigDecimal raw = base.multiply(discountValue)
                    .divide(HUNDRED, KRW_SCALE, KRW_ROUND);

            BigDecimal maxDiscount = coupon.getMaxDiscount();
            if (maxDiscount != null) {
                maxDiscount = maxDiscount.setScale(KRW_SCALE, KRW_ROUND);
                raw = raw.min(maxDiscount);
            }
            discount = raw;

        } else if ("fixed_amount".equalsIgnoreCase(discountType)) {
            discount = discountValue.setScale(KRW_SCALE, KRW_ROUND);
        }

        if (discount.compareTo(ZERO) < 0) discount = ZERO;
        if (discount.compareTo(base) > 0) discount = base;

        return discount;
    }

    // 공용쿠폰 사용 가능성 체크(필요에 따라 느슨/엄격 조정)
    private static boolean isUsableCommonCoupon(AvailableCoupon c) {
        if (c == null) return false;

        // (선택) 스택 규칙 → 상품쿠폰과의 중복 허용만 필요하면 주석 해제
        // if (Boolean.FALSE.equals(c.getIs_stackable())) return false;

        // (선택) 상태/수량/사용 여부
        if (c.getAmount() <= 0) return false;
        if (c.isUsed()) return false;
        if (c.getStatus() != null && !"Y".equalsIgnoreCase(c.getStatus())) return false;

        // (선택) 유효기간
        long now = System.currentTimeMillis();
        Timestamp start = c.getStartDate();
        Timestamp end = c.getEndDate();
        if (start != null && now < start.getTime()) return false;
        if (end != null && now > end.getTime()) return false;

        return true;
    }
    
    // --- helpers ---
    private static BigDecimal bd(int v) { return new BigDecimal(v).setScale(KRW_SCALE, KRW_ROUND); }
    private static BigDecimal nvl(BigDecimal v, BigDecimal d) { return v == null ? d : v; }
    private static String safe(String s) { return s == null ? "" : s; }
	
	
}
