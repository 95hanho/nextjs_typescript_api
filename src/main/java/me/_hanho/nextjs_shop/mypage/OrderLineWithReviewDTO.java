package me._hanho.nextjs_shop.mypage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderLineWithReviewDTO {
	
	private int orderListId; // 주문목록 id
    private int productDetailId; // 상품상세 id
    private int orderPrice; // 삼풍 id
    private String status; // 주문상태값 : CART, ORDERED, CANCELLED, PAID, SHIPPED, DELIVERED

    private Integer reviewId; // null이면 아직 안 씀
    private String reviewCreatedAt; // 
    private boolean canWrite;      // reviewId == null

    // (프론트 링크용) 
    public String getReviewLink() {
        return (reviewId != null)
            ? "/mypage/reviews/" + reviewId            // 리뷰 상세/수정
            : "/mypage/reviews/write?orderListId=" + orderListId; // 리뷰 작성
    }
    
}
