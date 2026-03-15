package me._hanho.nextjs_shop.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockHoldCoupon {
    private int holdCouponId;
    private int holdId;
    private int userCouponId;

}