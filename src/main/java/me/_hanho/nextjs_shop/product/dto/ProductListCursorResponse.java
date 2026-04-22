package me._hanho.nextjs_shop.product.dto;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductListCursorResponse {
    private Integer lastProductId;
    private Timestamp lastCreatedAt;
    private Integer lastPopularity;
    private Integer lastPrice;
}