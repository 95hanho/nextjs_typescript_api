package me._hanho.nextjs_shop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductMainSlideDto {
    private int productId;
    private String name;
    private Integer price;
    private Integer viewCount;
    private Integer wishCount;

    // 썸네일 정보
    private String img_path;
    private String copyright;
    private String copyright_url;
}