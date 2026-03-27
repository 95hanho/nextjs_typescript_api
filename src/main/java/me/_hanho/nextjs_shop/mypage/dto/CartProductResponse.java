package me._hanho.nextjs_shop.mypage.dto;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartProductResponse {

	private int cartId;
	private Timestamp createdAt;
	
	private int productOptionId;
	private int addPrice;
    private int stock;
    private String size;
    
    private int productId;
    private String productName;
    private int originPrice;
    private int finalPrice;
    
    private Integer wishId;
    
    private int fileId;
    private String fileName;
    private String storeName;
    private String filePath;
    private String copyright;
    private String copyrightUrl;
    
    private String sellerName;
    private int baseShippingFee; // 기본 배송비
    private int freeShippingMinAmount; // 무료배송 최소 주문금액
    private int extraShippingFee; // 제주/도서산간 추가 배송비
	
	private int quantity;
	private boolean selected;
}
