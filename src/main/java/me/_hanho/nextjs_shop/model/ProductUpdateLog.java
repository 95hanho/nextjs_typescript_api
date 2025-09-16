package me._hanho.nextjs_shop.model;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductUpdateLog {
    private int product_update_log_id;
    private int product_id;
    private String seller_id;
    private Timestamp updated_at;
    private String updated_detail;
}
