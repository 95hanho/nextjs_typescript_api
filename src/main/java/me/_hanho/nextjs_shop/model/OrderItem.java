package me._hanho.nextjs_shop.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItem {
    private int orderItemId;
    private int orderId;
    private int holdId;
    private String productName;
    private int count;
    private String size; // 'XS','S','M','L','XL','XXL'
    private int originPrice;
    private int finalPrice;
    private int addPrice;
    private BigDecimal couponDiscountedPrice;
    private BigDecimal totalPrice;

    private String status; // 상태값 'ORDERED','CANCELLED','PAID','SHIPPED','DELIVERED','PREPARING'
    private Timestamp shippingDate; // 발송일자
    private Timestamp deliveredDate; // 배송완료일자
    private Timestamp returnDate; // 반송일자
}
