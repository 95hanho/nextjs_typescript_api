package me._hanho.nextjs_shop.product.dto;

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
