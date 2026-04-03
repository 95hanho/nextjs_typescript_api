package me._hanho.nextjs_shop.mypage.dto;

import java.sql.Timestamp;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me._hanho.nextjs_shop.product.dto.ProductImageFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WishlistItemResponse {
	private int wishId;
	private Timestamp createdAt;
	
	private int productId;
	private String name;
	private int originPrice;
	private int finalPrice;
	private int likeCount;
	private int viewCount;
	private int wishCount;
	
	private String sellerName;
	
	private List<ProductImageFile> productImageList;

}
