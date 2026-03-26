package me._hanho.nextjs_shop.model;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemCoupon {
    private int orderItemCouponId;
    private int orderListId;
    private int userCouponId;
    private BigDecimal discountedPrice;
    private int couponId;
    private String description;
    private String couponCode;
    private String discountType;
    private BigDecimal discountValue;
    private BigDecimal maxDiscount;
    private BigDecimal minimumOrderBeforeAmount;
}
