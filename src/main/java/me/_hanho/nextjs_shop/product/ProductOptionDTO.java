package me._hanho.nextjs_shop.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductOptionDTO {
	private int productOptionId;
	private int productId;
	private int addPrice;
	private int stock;
	private String size;
}
