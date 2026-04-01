package me._hanho.nextjs_shop.mypage.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyOrderDetailItemCoupon {
    private int orderItemCouponId;
    private int orderItemId;
    private int userCouponId;
    private BigDecimal discountedPrice;
    private int couponId;
    private String description;
    private String couponCode;
    private String discountType;
    private BigDecimal discountValue;
    private BigDecimal maxDiscount;
    private BigDecimal minimumOrderBeforeAmount;
    //
    private Integer sellerNo;

}
