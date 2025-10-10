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
	private String user_id;
	private List<BuyProductDetailListRequest> buyList;
	private Product product;
}
