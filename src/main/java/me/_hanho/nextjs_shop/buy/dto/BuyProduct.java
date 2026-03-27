package me._hanho.nextjs_shop.buy.dto;

import lombok.Data;

@Data
public class BuyProduct {
	
	private int holdId;
	private int productOptionId;
	private int count;
	private int couponId;
}
