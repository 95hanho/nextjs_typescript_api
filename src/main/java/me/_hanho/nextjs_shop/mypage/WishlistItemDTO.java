package me._hanho.nextjs_shop.mypage;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WishlistItemDTO {
	private int wish_id;
	private Timestamp created_at;
	private String user_id;
	
	private int producct_id;
	private String name;
	private int price;
	private int view_count;
	private int wish_count;
	
	private int product_image_id;
	private int file_id;
	private String file_name;
	private String store_name;
	private String file_path;
	private String copyright;
	private String copyright_url;
	
	private String seller_id;
	private String seller_name;
	
	
	
}
