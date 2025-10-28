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
    private int product_image_id;
    private int product_id;
    private int file_id;
    private BigDecimal sort_key;
    private Timestamp created_at;
}
