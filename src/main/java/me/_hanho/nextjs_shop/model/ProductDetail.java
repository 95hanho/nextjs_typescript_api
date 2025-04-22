package me._hanho.nextjs_shop.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetail {
    private int product_detail_id;
    private int product_id;
    private String detail_name;
    private int add_price;
    private int stock;
    private Boolean add_product_status;
    private Date created_at;
    private String size;
    private String color;
    private int sales_count;
}
