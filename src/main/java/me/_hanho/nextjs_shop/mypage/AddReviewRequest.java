package me._hanho.nextjs_shop.mypage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddReviewRequest {
    private String content;
    private int rating;
    private int orderListId;
    private int productId;
    private int productOptionId;
    private Integer userNo;
}
