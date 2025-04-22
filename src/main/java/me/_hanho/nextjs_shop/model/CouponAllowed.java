package me._hanho.nextjs_shop.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CouponAllowed {
    private int coupon_allowed_id;
    private int coupon_id;
    private int product_id;
}