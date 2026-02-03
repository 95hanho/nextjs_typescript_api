package me._hanho.nextjs_shop.seller;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddProductRequest {
    @NotBlank private String name;
    @NotBlank private String colorName;
    @NotNull private Integer originPrice;
    @NotNull private Integer finalPrice;
    @NotNull private Integer menuSubId;
    @NotBlank private String materialInfo; // 제품 소재
    @NotBlank private String manufacturerName; // 제조자
    @NotBlank private String countryOfOrigin; // 제조국
    private String washCareInfo; // 세탁방법 및 주의사항
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate manufacturedYm; // 제조연월
    private String qualityGuaranteeInfo; // 품질보증기준s
    private String afterServiceContact; // A/S 책임자와 전화번호
    private String afterServiceManager; // (선택) A/S 책임자
    private String afterServicePhone; // (선택) A/S 전화번호
}
