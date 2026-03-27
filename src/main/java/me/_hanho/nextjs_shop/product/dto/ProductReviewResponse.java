package me._hanho.nextjs_shop.product.dto;

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
    private Timestamp reviewDate;
    private int rating;
    private int orderItemId;
    private Integer userNo;
    private String userName;
}
