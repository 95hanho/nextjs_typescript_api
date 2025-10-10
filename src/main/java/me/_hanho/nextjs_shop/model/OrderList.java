package me._hanho.nextjs_shop.model;

import java.math.BigDecimal;

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
    private BigDecimal order_price;
    private BigDecimal discount_price;
    private BigDecimal final_price;
    private int usercoupon_id;
}
