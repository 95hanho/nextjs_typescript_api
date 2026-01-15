package me._hanho.nextjs_shop.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductImageFile {
	
	private int productId;
	private int fileId;
	private String fileName;
	private String storeName;
	private String filePath;
	private String copyright;
	private String copyrightUrl;
}
