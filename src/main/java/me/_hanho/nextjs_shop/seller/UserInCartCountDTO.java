package me._hanho.nextjs_shop.seller;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInCartCountDTO {
	
	private Timestamp latest_date; // 가장 최근 장바구니에 넣은 날짜
	
	private int inCartCount;
	
	private String user_id;
	private String user_name;
	
	private String productNames; // 카트에 들어있는 상품(product.name)들 이름 문자배열(,)
	
	
}
