package me._hanho.nextjs_shop.product;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductReviewResponse {
	private int reviewId;
    private String content;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private int rating;
    private int orderListId;
    private int productId;
    private int productOptionId;
    private String userId;
}
