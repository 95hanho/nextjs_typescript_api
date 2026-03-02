package me._hanho.nextjs_shop.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartQtyRow {
    private Integer productOptionId;
    private Integer quantity;
}
