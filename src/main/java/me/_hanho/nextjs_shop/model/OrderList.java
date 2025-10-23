package me._hanho.nextjs_shop.model;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderList {
    private int orderListId;
    private int orderId;
    private int count;
    private BigDecimal orderPrice;
    private BigDecimal discountPrice;
    private BigDecimal finalPrice;
    
    private int usercouponId;
    private int holdId;
}
