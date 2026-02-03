package me._hanho.nextjs_shop.mypage;

import java.math.BigDecimal;
import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCouponResponse {

	private int userCouponId;
	private boolean used;
	
	private int couponId;
	private String description;
	private String couponCode;
    private String discountType;
    private BigDecimal discountValue;
    private BigDecimal maxDiscount;
    private BigDecimal minimumOrderBeforeAmount;
    private String status;
    private Boolean isStackable;
    private Boolean isProductRestricted;
    private int amount;
    private Timestamp startDate;
    private Timestamp endDate;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    
    private String sellerName;
	
}
