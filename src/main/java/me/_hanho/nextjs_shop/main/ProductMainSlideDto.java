package me._hanho.nextjs_shop.main;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductMainSlideDto {
    private int productId;
    private String name;
    private int originPrice;
    private int finalPrice;
    private int viewCount;
    private int wishCount;

    // 썸네일 정보
    private String imgPath;
    private String copyright;
    private String copyrightUrl;
}