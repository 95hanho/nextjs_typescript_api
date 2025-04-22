package me._hanho.nextjs_shop.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data // getter/setter, toString, equals, hashCode 자동 생성
@NoArgsConstructor // 기본 생성자
@AllArgsConstructor // 전체 필드 생성자
public class Coupon {
	
    private int coupon_id;
    private String description;
    private String coupon_code;
    private String discount_type;
    private String discount_value;
    private Integer minimum_order_before_amount;
    private Integer minimum_order_after_amount;
    private Boolean is_stackable;
    private Date start_date;
    private Date end_date;
    private String status;
    private Date created_at;
    private Date updated_at;
    private String user_id;
    
}