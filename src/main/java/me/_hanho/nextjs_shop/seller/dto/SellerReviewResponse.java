package me._hanho.nextjs_shop.seller.dto;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SellerReviewResponse {
    private int reviewId;
    private String content;
    private int rating;
    private Timestamp createdAt;

    private String userName;

    private int productId;
    private String productName;

    /*  */
    private int reviewImageId;

    private int fileId;
	private String fileName;
	private String storeName;
	private String filePath;
	private String copyright;
	private String copyrightUrl;
}
