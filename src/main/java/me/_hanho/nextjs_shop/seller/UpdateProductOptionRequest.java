package me._hanho.nextjs_shop.seller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProductOptionRequest {
    private int productOptionId;
    private int addPrice;
    private int stock;
}
