package me._hanho.nextjs_shop.mypage;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartProductDTO {

	private int cartId;
	private Timestamp createdAt;
	
	private int productOptionId;
	private int addPrice;
    private int stock;
    private String size;
    
    private int productId;
    private String productName;
    private int price;
    
    private Integer wishId;
    
    private int fileId;
    private String fileName;
    private String storeName;
    private String filePath;
    private String copyright;
    private String copyrightUrl;
    
    private String sellerName;
	
	private int quantity;
	private boolean selected;
}
