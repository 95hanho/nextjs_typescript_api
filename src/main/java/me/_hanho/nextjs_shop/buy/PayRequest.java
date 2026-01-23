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
	private BigDecimal eachCouponDiscountTotal; // 각 상품쿠폰 할인값 총합
    private BigDecimal commonCouponDiscountTotal; // 공용쿠폰 할인값 총합
    private int shippingFee; // 배송비
	private int usedMileage; // 사용된 마일리지
	private int remainingMileague; // 남은마일리지
	private BigDecimal totalFinal; // 총가격(배송비 포함)
	private String paymentMethod; // 결제 방식
	
	private int userCouponId;
	private int addressId;
}
