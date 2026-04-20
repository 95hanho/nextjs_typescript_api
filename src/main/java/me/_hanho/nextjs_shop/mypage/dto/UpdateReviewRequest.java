package me._hanho.nextjs_shop.mypage.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateReviewRequest {
    @NotNull private Integer reviewId; // 리뷰 ID
    @NotBlank private String content;
    @NotNull private Integer rating;
}
