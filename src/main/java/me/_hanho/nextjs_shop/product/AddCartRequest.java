package me._hanho.nextjs_shop.product;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddCartRequest {
    private List<AddCartItem> addCartList;
    private Integer productId;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class AddCartItem {
    private Integer productOptionId;
    private Integer quantity;
}