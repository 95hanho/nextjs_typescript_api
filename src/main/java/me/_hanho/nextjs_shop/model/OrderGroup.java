package me._hanho.nextjs_shop.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderGroup {
    private int order_id;
    private Timestamp order_date;
    private String user_id;
    private BigDecimal total_price;
    private BigDecimal shipping_fee;
    private String payment_method;
    private String payment_code;
    private BigDecimal discount_price;
    private String status;
    private int usercoupon_id;
    private Timestamp shipping_date;
    private Timestamp delivered_date;
    private Timestamp return_date;
    private int address_id;
}
