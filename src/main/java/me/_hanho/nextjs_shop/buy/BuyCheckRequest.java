package me._hanho.nextjs_shop.buy;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me._hanho.nextjs_shop.model.Product;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BuyCheckRequest {
	private Integer userNo;
	private List<BuyProductOptionListRequest> buyList;
	private Product product;
}
