package me._hanho.nextjs_shop.mypage.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponse {
    private int reviewId;
    private String content;
    private int rating;

    private List<ReviewImageResponse> reviewImages;
}
