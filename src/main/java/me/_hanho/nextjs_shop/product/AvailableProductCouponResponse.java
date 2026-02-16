package me._hanho.nextjs_shop.product;

import java.math.BigDecimal;
import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AvailableProductCouponResponse {
	private int couponId;
	private String description;
    private String discountType;
    private BigDecimal discountValue;
    private BigDecimal maxDiscount;
    private BigDecimal minimumOrderBeforeAmount;
    private Boolean isStackable;
    private Boolean isProductRestricted;
    private int amount;
    private Timestamp startDate;
    private Timestamp endDate;
    private String issueMethod;
    
    private Integer couponAllowedId;
    
	private Integer userCouponId;
    
    private String sellerName;
}
