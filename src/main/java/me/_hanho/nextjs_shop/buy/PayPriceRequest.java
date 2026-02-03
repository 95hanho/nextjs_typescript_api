package me._hanho.nextjs_shop.buy;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PayPriceRequest {
	
	private List<BuyProduct> products;
	private AvailableCouponResponse commonCoupon;
	private int useMileage; // 사용마일리지
	
}
