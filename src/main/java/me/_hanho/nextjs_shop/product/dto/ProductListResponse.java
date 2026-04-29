package me._hanho.nextjs_shop.product.dto;

import java.sql.Timestamp;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductListResponse {
	private int productId;
	private String name;
	private String colorName; // 'BLACK','WHITE','GRAY','NAVY','BEIGE','RED','PINK','ORANGE','YELLOW','GREEN','KHAKI','MINT','BLUE','SKYBLUE','PURPLE','BROWN','IVORY','CHARCOAL','DENIM'
	private int originPrice;
	private int finalPrice;
	private Timestamp createdAt;
	private int viewCount;
	private int wishCount;
	
	private String sellerName;

	private boolean soldOut; // 재고 소진 여부

	private Integer wishId; // 찜 여부 (찜이 존재하면 wishId, 존재하지 않으면 null)
	
	private List<ProductImageFile> productImageList;
	
}
