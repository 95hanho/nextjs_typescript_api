package me._hanho.nextjs_shop.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductImage {
    private int productImageId;
    private int productId;
    private int fileId;
    private BigDecimal sortKey;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
