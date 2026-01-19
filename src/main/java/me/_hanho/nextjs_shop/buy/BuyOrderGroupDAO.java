package me._hanho.nextjs_shop.buy;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BuyOrderGroupDAO {
    private int userNo;
    private BigDecimal eachCouponDiscountTotal; // 각 상품쿠폰 할인값 총합
    private BigDecimal commonCouponDiscountTotal; // 공용쿠폰 할인값 총합
    private BigDecimal shippingFee; // 배송비
    private int usedMileage; // 사용된 마일리지
    private int remainingMileage; // 남은 마일리지
    private BigDecimal totalPrice; // 총합 금액
    private String paymentMethod; // 결제 방식
    private String paymentCode; // 결제 코드
    private String status; // 상태값 'ORDERED','CANCELLED','PAID','SHIPPED','DELIVERED','PREPARING'
    private int userCouponId;
    private int addressId;
}
