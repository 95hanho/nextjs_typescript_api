package me._hanho.nextjs_shop.product.dto;

import java.sql.Timestamp;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetProductListRequest {

    @NotBlank(message = "sort는 필수입니다.")
    private String sort; // POPULAR | LATEST | PRICE_LOW | PRICE_HIGH

    // POPULAR 정렬일 때만 유효
    private String popularPeriod; // DAYS_7 | DAYS_30 | YEAR_1 | ALL

    @NotNull(message = "menuSubId는 필수입니다.")
    private Integer menuSubId;

    private String lastCreatedAt;
    private Integer lastProductId;
    private Integer lastPopularity;
    private Integer lastPrice;
}