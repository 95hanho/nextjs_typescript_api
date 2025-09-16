package me._hanho.nextjs_shop.mypage;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MyOrderWithReviewDTO {

    private int orderId;
    private String orderDate; // 주문일자
    private int totalPrice; // 총 주문금액
    private int shippingFee; // 배송비
    private int discountPrice; // 할인된 금액
    private String paymentMethod; // 결제 방식

    private List<OrderLineWithReviewDTO> items;
}
