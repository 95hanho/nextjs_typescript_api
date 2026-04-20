package me._hanho.nextjs_shop.mypage.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddReviewFileMeta {
    private String clientKey;
	private Integer sortKey;
	private String fileName;

    private Integer fileId; // 파일 ID (업로드 후 반환되는 값)
}
