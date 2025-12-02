package me._hanho.nextjs_shop.model;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    private int productId;
    private String name;
    private String colorName;
    private int price;
    private Timestamp createdAt;
    private int likeCount;
    private int viewCount;
    private int wishCount;
    private boolean saleStop;
    private String sellerId;
    private int menuSubId;
}
