package me._hanho.nextjs_shop.product.dto;

import java.sql.Timestamp;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me._hanho.nextjs_shop.mypage.dto.ReviewImageResponse;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductReviewResponse {
	private int reviewId;
    private String content;
    private Timestamp reviewDate;
    private int rating;
    private int orderItemId;
    private String userName;

    private List<ReviewImageResponse> reviewImages;
}
