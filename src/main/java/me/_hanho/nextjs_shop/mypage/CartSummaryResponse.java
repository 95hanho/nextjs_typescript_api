package me._hanho.nextjs_shop.mypage;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartSummaryResponse {
    private List<CartProductResponse> cartList;
    private List<Integer> productIds;
    private boolean exceedQuantity;
}