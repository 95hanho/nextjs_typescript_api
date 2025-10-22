package me._hanho.nextjs_shop.buy;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me._hanho.nextjs_shop.model.Coupon;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PayPriceRequest {
	
	private List<BuyProduct> products;
	private AvailableCoupon commonCoupon;
	private String user_id;
	private int useMileage; // 사용마일리지
	
}
 