package me._hanho.nextjs_shop.model;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderGroup {
    private int order_id;
    private Timestamp order_date;
    private String user_id;
    private int total_price;
    private int shipping_fee;
    private String payment_method;
    private int discount_price;
}
