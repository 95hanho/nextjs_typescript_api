package me._hanho.nextjs_shop.product.dto;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OtherProduct {
	private int productId;
	private String name;
	private String colorName; // 'BLACK','WHITE','GRAY','NAVY','BEIGE','RED','PINK','ORANGE','YELLOW','GREEN','KHAKI','MINT','BLUE','SKYBLUE','PURPLE','BROWN','IVORY','CHARCOAL','DENIM'
	private int originPrice;
	private int finalPrice;
	private Timestamp createdAt;
	private int viewCount;
	private int wishCount;
	
	private String sellerName;
	
	private int fileId;
	private String fileName;
	private String storeName;
	private String filePath;
	private String copyright;
	private String copyrightUrl;

	private boolean isWished; // 사용자가 찜한 상품인지 여부
	
}
