package me._hanho.nextjs_shop.buy;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BuyProductOptionListRequest {
	private int productOptionId;
	private int addPrice;
	private int count;
	
}
