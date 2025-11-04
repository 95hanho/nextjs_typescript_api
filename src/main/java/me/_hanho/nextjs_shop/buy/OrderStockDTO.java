package me._hanho.nextjs_shop.buy;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderStockDTO {
	private int holdId;
	private int count;
	
	private int productDetailId;
	private int addPrice;
	private String size;
	
	private int productId;
	private String name;
	private String colorName;
	
	private String sellerId;
	private String sellerName;
	
	private String fileName;
	private String storeName;
	private String filePath;
	private String copyright;
	private String copyrightUrl;
}
