package me._hanho.nextjs_shop.mypage.dto;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MyOrderGroupResponse {

    private int orderId; // 주문 ID
    private Timestamp orderDate; // 주문일자

    private List<MyOrderItemResponse> items; // 주문 상품 목록
    
}
