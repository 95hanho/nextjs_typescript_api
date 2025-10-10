package me._hanho.nextjs_shop.buy;

import lombok.Data;

@Data
public class BuyProduct {
	
	private int hold_id;
	private int product_detail_id;
	private int count;
	private int coupon_id;
}
