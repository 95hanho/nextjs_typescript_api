package me._hanho.nextjs_shop.buy;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductWithCouponsDTO {
	private int holdId;
	private int count;

	private int productDetailId;
	private int addPrice;
	
	private Integer userCouponId;
	
	private int productId;
	private int price;
	
	private int couponId;
	private String description;
	private String discountType; // 할인유형 'percentage', 'fixed_amount'
	private BigDecimal discountValue; // 할인 비율/값
	private BigDecimal maxDiscount; // 최대 할인값
	private BigDecimal minimumOrderBeforeAmount; // 할인 전 쿠폰 사용을 위한 최소 주문금액
	private boolean isStackable;
	private boolean isProductRestricted;
	
	private BigDecimal discountAmount;   // 적용된 총 할인 금액
	private BigDecimal finalPrice;       // 최종 결제 금액 (쿠폰 적용)
}
