package me._hanho.nextjs_shop.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    private int product_id;
    private String name;
    private Integer price;
    private Date created_at;
    private Integer view_count;
    private Integer wish_count;
    private String seller_id;
    private Integer menu_sub_id;
}
