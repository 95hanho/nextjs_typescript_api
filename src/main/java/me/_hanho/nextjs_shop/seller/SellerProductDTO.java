package me._hanho.nextjs_shop.seller;

import java.sql.Timestamp;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me._hanho.nextjs_shop.model.ProductDetail;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SellerProductDTO {
	private int product_id;
	private String name;
	private String color_name;
	private int price;
	private Timestamp created_at;
	private int view_count;
	private int wish_count;
	
	private String seller_id;
	
	private int menu_sub_id;
	private String sub_menu_name;
	private String top_menu_name;
	private String gender;
	
	private List<ProductDetail> detailList;
	
}
