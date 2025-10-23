package me._hanho.nextjs_shop.mypage;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MyOrderGroupDTO {

    private int order_id; // 
    private Timestamp order_date; // 주문일자
    private String user_id; // 유저아이디
    private BigDecimal eachcoupon_discount_total; // 각 상품쿠폰 할인값 총합
    private BigDecimal commoncoupon_discount_total; // 공용쿠폰 할인값 총합
    private int shipping_fee; // 배송비
    private int used_mileage; // 사용된 마일리지
    private BigDecimal total_price; // 총합 금액
    private String payment_method; // 결제 방식
    private String payment_code; // 결제 코드
    private String status; // 상태값
    private Timestamp shipping_date; // 발송일지
    private Timestamp delivered_date; // 배송완료일자
    private Timestamp return_date; // 반송일자
    
    private List<OrderListWithReviewDTO> items;
}
