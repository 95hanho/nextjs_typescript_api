package me._hanho.nextjs_shop.buy.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ManageStockHoldCoupon {
    private Integer holdId;
    private Integer userCouponId;
}