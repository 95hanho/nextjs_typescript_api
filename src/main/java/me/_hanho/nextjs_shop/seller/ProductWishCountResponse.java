package me._hanho.nextjs_shop.seller;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductWishCountResponse {
	private Timestamp latestDate; // 가장 최근위시 날짜
	
	private String userId; // 유저아이디
	private String userName; // 유저이름
	//	private int product_id;
	//	private int wish_count; // 이 유저가 나의 상품을 위시한 수 = 밑에 리스트 길이
	private String productNames; // 위시한 상품들 이름 리스트
	
}
