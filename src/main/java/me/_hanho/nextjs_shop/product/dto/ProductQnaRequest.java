package me._hanho.nextjs_shop.product.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductQnaRequest {
    @NotBlank private String question;
    @NotNull private Integer productQnaTypeId;
    @NotNull private Boolean secret;
}
