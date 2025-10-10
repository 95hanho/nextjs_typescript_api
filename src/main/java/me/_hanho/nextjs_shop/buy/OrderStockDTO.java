package me._hanho.nextjs_shop.buy;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderStockDTO {
	private int hold_id;
	private int count;
	
	private int product_detail_id;
	private int add_price;
	private String size;
	
	private int product_id;
	private String name;
	private String color_name;
	
	private String seller_id;
	private String seller_name;
	
	private String file_name;
	private String store_name;
	private String file_path;
	private String copyright;
	private String copyright_url;
}
