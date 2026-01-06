package me._hanho.nextjs_shop.model;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductOption {
    private int productOptionId;
    private int productId;
    private int addPrice;
    private int stock;
    private Timestamp createdAt;
    private String size;
    private int salesCount;
}
