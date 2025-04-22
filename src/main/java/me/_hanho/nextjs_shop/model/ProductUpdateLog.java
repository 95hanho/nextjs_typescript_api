package me._hanho.nextjs_shop.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductUpdateLog {
    private int product_update_log_id;
    private int product_id;
    private String seller_id;
    private Date updated_at;
    private String updated_detail;
}
