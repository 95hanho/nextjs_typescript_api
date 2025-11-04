package me._hanho.nextjs_shop.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderGroup {
    private int orderId; // 
    private Timestamp orderDate; // 주문일자
    private String userId; // 유저아이디
    private BigDecimal eachCouponDiscountTotal; // 각 상품쿠폰 할인값 총합
    private BigDecimal commonCouponDiscountTotal; // 공용쿠폰 할인값 총합
    private int shippingFee; // 배송비
    private int usedMileage; // 사용된 마일리지
    private int remainingMileage; // 남은 마일리지
    private BigDecimal totalPrice; // 총합 금액
    private String paymentMethod; // 결제 방식
    private String paymentCode; // 결제 코드
    private String status; // 상태값
    private Timestamp shippingDate; // 발송일지
    private Timestamp deliveredDate; // 배송완료일자
    private Timestamp returnDate; // 반송일자
    
    private int userCouponId;
    private int addressId;
}
