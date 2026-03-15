package me._hanho.nextjs_shop.buy;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BuyProductOptionList {
	private int productOptionId;
	private int cartId;
	private int count;
	private List<Integer> couponIds; // optional, 구매 시점에 선택한 쿠폰들 (holdId는 아직 없으니 프론트에서 holdId 없이 보냄)
	
}
