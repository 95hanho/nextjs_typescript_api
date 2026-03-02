package me._hanho.nextjs_shop.product;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartAddResult {
    private int successCount;
    private boolean partial;
    private List<Integer> limitedItems; // 재고 제한된 옵션들
}