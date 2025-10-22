package me._hanho.nextjs_shop.buy;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PayRequest {
	
	private List<ProductWithCouponsDTO> items;
	private String user_id;
	private BigDecimal eachcoupon_discount_total; // 각 상품쿠폰 할인값 총합
    private BigDecimal commoncoupon_discount_total; // 공용쿠폰 할인값 총합
    private int shipping_fee; // 배송비
	private int used_Mileage; // 사용된 마일리지
	private int remaining_mileague; // 남은마일리지
	private BigDecimal totalFinal; // 총가격(배송비 포함)
	private String payment_method; // 결제 방식
	
	private int usercoupon_id;
	private int address_id;
}
