package me._hanho.nextjs_shop.mypage.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewImageResponse {
    private int reviewImageId;
    private int reviewId;
    private BigDecimal sortKey;

    private int fileId;
	private String fileName;
	private String storeName;
	private String filePath;
	private String copyright;
	private String copyrightUrl;
}
