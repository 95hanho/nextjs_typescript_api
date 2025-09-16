package me._hanho.nextjs_shop.seller;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SellerCouponAllowedProductDTO {
    private int coupon_allowed_id;
    private int coupon_id;
    private int product_id;
    
    private String name;
    private Timestamp created_at;
}
