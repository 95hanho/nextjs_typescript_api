package me._hanho.nextjs_shop.seller;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductViewCountResponse {
	private String userId;
	private String userName;
	
	private int productId;
	private String productName;
	
	private int viewCount;
	private Timestamp latestDate;
}
