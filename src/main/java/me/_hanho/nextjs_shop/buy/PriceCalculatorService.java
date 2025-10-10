package me._hanho.nextjs_shop.buy;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

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
        BigDecimal minOrder = nvl(dto.getMinimum_order_before_amount(), ZERO).setScale(KRW_SCALE, KRW_ROUND);
        BigDecimal discountValue = nvl(dto.getDiscount_value(), ZERO);
        String discountType = safe(dto.getDiscount_type());

        // 소계 계산: (price + add_price) * count
        BigDecimal unitBase = bd(dto.getPrice()).add(bd(dto.getAdd_price())).setScale(KRW_SCALE, KRW_ROUND);
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
                BigDecimal maxDiscount = dto.getMax_discount();
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
        dto.setFinalPrice(finalPrice);
    }

    // --- helpers ---
    private static BigDecimal bd(int v) { return new BigDecimal(v).setScale(KRW_SCALE, KRW_ROUND); }
    private static BigDecimal nvl(BigDecimal v, BigDecimal d) { return v == null ? d : v; }
    private static String safe(String s) { return s == null ? "" : s; }
	
	
}
