package me._hanho.nextjs_shop.mypage.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewOrderInfoResponse {
    private int orderItemId;
    private int orderId;
    private String productName;
    private int count;
    private String size; // 'XS','S','M','L','XL','XXL'
    private int originPrice;
    private int finalPrice;
    private int addPrice;
    private BigDecimal couponDiscountedPrice;
    private BigDecimal totalPrice;
    private String status; // 상태값 'ORDERED','CANCELLED','SHIPPED','DELIVERED','PREPARING'

    private int holdId;
    private int productOptionId;
    private int productId;

    private int sellerNo;
    private String sellerName;
    private String sellerNameEn;
    
    private int productImageId;
    private int fileId;
    private String fileName;
    private String storeName;
    private String filePath;
    private String copyright;
    private String copyrightUrl;
    private String fileExtension;

}
