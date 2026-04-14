package me._hanho.nextjs_shop.seller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateFile {
	private Integer productImageId;
	private Integer sortKey;
}