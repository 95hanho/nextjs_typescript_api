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
    private int price;
    private Date created_at;
    private int view_count;
    private int wish_count;
    private String seller_id;
    private int menu_sub_id;
}
