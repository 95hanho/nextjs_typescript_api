package me._hanho.nextjs_shop.buy;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BuyProductDetailListRequest {
	private int product_detail_id;
	private int add_price;
	private Integer usercoupon_id;
	private int count;
	
	
}
