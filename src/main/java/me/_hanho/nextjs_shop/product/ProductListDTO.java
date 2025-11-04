package me._hanho.nextjs_shop.product;

import java.sql.Timestamp;

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
	
	private int fileId;
	private String fileName;
	private String storeName;
	private String filePath;
	private String copyright;
	private String copyrightUrl;
}
