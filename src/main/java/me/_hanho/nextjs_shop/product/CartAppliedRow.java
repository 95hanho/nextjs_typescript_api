package me._hanho.nextjs_shop.product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartAppliedRow {
    private Integer productOptionId;
    private Integer stock;
    private Integer cartQuantity;  // 최종 장바구니 수량
    private Integer appliedDelta;  // 이번 요청으로 실제 반영된 증가량(0~요청수량)
}