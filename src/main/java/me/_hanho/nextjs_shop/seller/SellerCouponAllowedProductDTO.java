package me._hanho.nextjs_shop.seller;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SellerCouponAllowedProductDTO {
    private int couponAllowedId;
    private int couponId;
    private int productId;
    
    private String name;
    private Timestamp createdAt;
}
