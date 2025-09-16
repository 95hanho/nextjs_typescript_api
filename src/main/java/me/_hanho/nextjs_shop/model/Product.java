package me._hanho.nextjs_shop.model;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    private int product_id;
    private String name;
    private String color_name;
    private int price;
    private Timestamp created_at;
    private int view_count;
    private int wish_count;
    private boolean sale_stop;
    private String seller_id;
    private int menu_sub_id;
}
