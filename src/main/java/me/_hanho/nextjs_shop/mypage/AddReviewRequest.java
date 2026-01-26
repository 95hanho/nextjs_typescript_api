package me._hanho.nextjs_shop.mypage;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddReviewRequest {
    @NotBlank private String content;
    @NotNull private Integer rating;
    @NotNull private Integer orderListId;
    @NotNull private Integer productId;
    @NotNull private Integer productOptionId;
}
