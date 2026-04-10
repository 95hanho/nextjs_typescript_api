package me._hanho.nextjs_shop.product.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductOptionResponse {
	private int productOptionId;
	private int productId;
	private int addPrice;
	private int stock;
	private String size; // 'XS','S','M','L','XL','XXL'
}
