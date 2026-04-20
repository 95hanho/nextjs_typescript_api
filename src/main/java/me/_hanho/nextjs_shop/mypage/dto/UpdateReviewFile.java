package me._hanho.nextjs_shop.mypage.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateReviewFile {
    private Integer reviewImageId;
	private Integer sortKey;
}
