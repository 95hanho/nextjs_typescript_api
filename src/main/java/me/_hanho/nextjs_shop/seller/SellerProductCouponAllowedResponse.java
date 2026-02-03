package me._hanho.nextjs_shop.seller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SellerProductCouponAllowedResponse {
	private int productId;
	private String productName;
	
    private Integer couponAllowedId;
    private Integer couponId;
}
