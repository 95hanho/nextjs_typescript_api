package me._hanho.nextjs_shop.buy;

import lombok.Data;

@Data
public class BuyProduct {
	
	private int holdId;
	private int productDetailId;
	private int count;
	private int couponId;
}
