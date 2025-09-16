package me._hanho.nextjs_shop.seller;

import java.sql.Timestamp;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductWishCountDTO {
	
	private Timestamp latest_date; // 가장 최근위시 날짜
	
	private String user_id; // 유저아이디
	private String user_name; // 유저이름
	//	private int product_id;
	//	private int wish_count; // 이 유저가 나의 상품을 위시한 수 = 밑에 리스트 길이
	private String productNames; // 위시한 상품들 이름 리스트
	
}
