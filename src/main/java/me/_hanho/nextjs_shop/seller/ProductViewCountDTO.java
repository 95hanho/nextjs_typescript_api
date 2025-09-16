package me._hanho.nextjs_shop.seller;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductViewCountDTO {

	private String user_id;
	private String user_name;
	
	private int product_id;
	private String product_name;
	
	private int view_count;
	private Timestamp latest_date;
}
