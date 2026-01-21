package me._hanho.nextjs_shop.seller;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProductOptionRequest {
	@NotNull private Integer productOptionId;
	@NotNull private Integer addPrice;
	@NotNull private Integer stock;
    @NotNull private Boolean isDisplayed;
}
