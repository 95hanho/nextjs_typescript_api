package me._hanho.nextjs_shop.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductReviewSummary {
	private Double avgRating;   // 소수점 필요
    private Integer reviewCount;
}
