package me._hanho.nextjs_shop.seller;

import java.sql.Timestamp;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me._hanho.nextjs_shop.model.ProductOption;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SellerProductDTO {
	private int productId;
	private String name;
	private String colorName;
	private int price;
	private Timestamp createdAt;
	private int viewCount;
	private int wishCount;
	
	private String sellerId;
	
	private String subMenuName;
	private String topMenuName;
	private String gender;
	
	private List<ProductOption> detailList;
	
}
