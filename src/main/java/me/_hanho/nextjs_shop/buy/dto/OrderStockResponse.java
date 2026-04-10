package me._hanho.nextjs_shop.buy.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderStockResponse {
	private int holdId;
	private int count;
	
	private int productOptionId;
	private int addPrice;
	private String size; // 'XS','S','M','L','XL','XXL'
	
	private int productId;
	private String productName;
	private int originPrice;
	private int finalPrice;
	private String colorName;

	private Integer wishId; // 찜 여부 확인용 (null이면 찜 안한 것)
	
	private String sellerName;
	private int baseShippingFee; // 기본 배송비
    private int freeShippingMinAmount; // 무료배송 최소 주문금액
    private int extraShippingFee; // 제주/도서산간 추가 배송비
	
	private String fileName;
	private String storeName;
	private String filePath;
	private String copyright;
	private String copyrightUrl;
}
