package me._hanho.nextjs_shop.main.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductMainSlideResponse {
    private int productId;
    private String productName;
    private int originPrice;
    private int finalPrice;
    private int viewCount;
    private int wishCount;

    // 썸네일 정보
    private int fileId;
    private String fileName;
    private String storeName;
    private String filePath;
    private String copyright;
    private String copyrightUrl;
    private String fileExtension;

    private String sellerName; // 판매자 이름

    private Integer wishId; // 위시 여부 확인용 (null이면 위시 안한 것)
}