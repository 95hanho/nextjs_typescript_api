package me._hanho.nextjs_shop.model;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItem {
    private int orderListId;
    private int orderId;
    private int holdId;
    private String product_name;
    private int count;
    private String size;
    private int orderPrice;
    private int finalPrice;
    private int addPrice;
    private BigDecimal couponDiscountedPrice;
    private BigDecimal totalPrice;
}
