package me._hanho.nextjs_shop.product;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductListDTO {
	private int product_id;
	private String name;
	private String color_name;
	private int price;
	private Timestamp created_At;
	private int view_count;
	private int wish_count;
	
	private String seller_id;
	private String seller_name;
	
	private int file_id;
	private String file_name;
	private String store_name;
	private String file_path;
	private String copyright;
	private String copyright_url;
}
