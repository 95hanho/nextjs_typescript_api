package me._hanho.nextjs_shop.mypage;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WishlistItemDTO {
	private int wishId;
	private Timestamp createdAt;
	
	private int productId;
	private String name;
	private int price;
	private int likeCount;
	private int viewCount;
	private int wishCount;
	
	private int productImageId;
	private int fileId;
	private String fileName;
	private String storeName;
	private String filePath;
	private String copyright;
	private String copyrightUrl;
	
	private String sellerName;
	
	
	
}
