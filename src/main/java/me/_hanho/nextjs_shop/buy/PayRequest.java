package me._hanho.nextjs_shop.buy;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me._hanho.nextjs_shop.model.UserAddress;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PayRequest {
	
	private List<ProductWithCouponsDTO> items;
	private String user_id;
	private UserAddress deliveryAddress; // 주문주소
	private int useMileage; // 사용마일리지
	private int shipping_fee; // 배송비
	private String payment_method; // 결제 방식
	
	private BigDecimal deliveryFee; // 배송비
	private BigDecimal totalDiscount; // 총할인 가격
	private BigDecimal totalFinalBeforeDelivery; // 총가격(배송비 미포함)
	private BigDecimal totalFinal; // 총가격(배송비 포함)
	
}
