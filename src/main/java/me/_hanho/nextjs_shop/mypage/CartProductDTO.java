package me._hanho.nextjs_shop.mypage;

import java.sql.Timestamp;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartProductDTO {

	private int cart_id;
	private Timestamp created_at;
	
	private int product_detail_id;
	private int add_price;
    private int stock;
    private String size;
    
    private int product_id;
    private String product_name;
    private int price;
//    private boolean sale_stop;
    
    private int file_id;
    private String file_name;
    private String store_name;
    private String file_path;
    private String copyright;
    private String copyright_url;
    
    private String seller_id;
    private String seller_name;
	
	private int quantity;
	private boolean selected;
}
