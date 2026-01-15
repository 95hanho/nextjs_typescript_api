package me._hanho.nextjs_shop.product;

import java.sql.Timestamp;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductListDTO {
	private int productId;
	private String name;
	private String colorName;
	private int price;
	private Timestamp createdAt;
	private int viewCount;
	private int wishCount;
	
	private String sellerId;
	private String sellerName;
	
	private List<ProductImageFile> productImageList;
	
}
