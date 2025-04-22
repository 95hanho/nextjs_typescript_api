package me._hanho.nextjs_shop.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderGroup {
    private int order_id;
    private Date order_date;
    private String user_id;
    private String status;
    private int total_price;
    private int shipping_fee;
    private String payment_method;
    private int discount_price;
}
