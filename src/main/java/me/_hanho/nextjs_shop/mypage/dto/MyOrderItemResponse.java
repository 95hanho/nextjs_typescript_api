package me._hanho.nextjs_shop.mypage.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MyOrderItemResponse {
    
    private int orderItemId;
    private int orderId;
    private int holdId;
    private String productName;
    private int count;
    private String size; // 'XS','S','M','L','XL','XXL'
    private int originPrice;
    private int finalPrice;
    private int addPrice;
    private String status; // 상태값 'ORDERED','CANCELLED','PAID','SHIPPED','DELIVERED','PREPARING'

    private int reviewId;

    private int sellerNo;
    private String sellerName;
    private String sellerNameEn;

    private int fileId;
    private String fileName;
    private String storeName;
    private String filePath;
    private String copyright;
    private String copyrightUrl;
    private String fileExtension;

}
