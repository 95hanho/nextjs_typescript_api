package me._hanho.nextjs_shop.seller;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddProductOptionRequest {
    @NotNull private Integer productId;
    @NotNull private Integer addPrice;
    @NotNull private Integer stock;
    @NotBlank private String size;
}
