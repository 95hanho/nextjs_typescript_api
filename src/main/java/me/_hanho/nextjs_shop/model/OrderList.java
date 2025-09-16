package me._hanho.nextjs_shop.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderList {
    private int order_list_id;
    private int product_detail_id;
    private int order_id;
    private int order_price;
    private String status;
}
