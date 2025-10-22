package me._hanho.nextjs_shop.buy;

import java.math.BigDecimal;
import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AvailableCoupon {
	
    private int coupon_id;
    private String description;
    private String coupon_code;
    private String discount_type;
    private BigDecimal discount_value;
    private BigDecimal max_discount;
    private BigDecimal minimum_order_before_amount;
    private String status;
    private Boolean is_stackable;
    private Boolean is_product_restricted;
    private int amount;
    private Timestamp start_date;
    private Timestamp end_date;
    private Timestamp created_at;
    private Timestamp updated_at;
    private String seller_id;
    //
    private int usercoupon_id; 
	private boolean used; 
	private String user_id;
    
	
}
