package me._hanho.nextjs_shop.product.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddCartItem {
    private Integer productOptionId;
    private Integer quantity;
}