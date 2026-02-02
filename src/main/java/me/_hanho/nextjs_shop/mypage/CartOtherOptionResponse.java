package me._hanho.nextjs_shop.mypage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartOtherOptionResponse {
    private int productOptionId;
    private int productId;
    private int addPrice;
    private int stock;
    private String size;
}
