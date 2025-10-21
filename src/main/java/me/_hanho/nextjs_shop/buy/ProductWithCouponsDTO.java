package me._hanho.nextjs_shop.buy;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductWithCouponsDTO {
	private int hold_id;
	private int count;

	private int product_detail_id;
	private int add_price;
	
	private int usercoupon_id;
	
	private int product_id;
	private int price;
	
	private int coupon_id;
	private String description;
	private String discount_type; // 할인유형 'percentage', 'fixed_amount'
	private BigDecimal discount_value; // 할인 비율/값
	private BigDecimal max_discount; // 최대 할인값
	private BigDecimal minimum_order_before_amount; // 할인 전 쿠폰 사용을 위한 최소 주문금액
	private boolean is_stackable;
	private boolean is_product_restricted;
	
	private BigDecimal discountAmount;   // 적용된 총 할인 금액
	private BigDecimal finalPrice;       // 최종 결제 금액 (쿠폰 적용)
}
