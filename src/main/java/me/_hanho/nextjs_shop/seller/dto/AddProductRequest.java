package me._hanho.nextjs_shop.seller.dto;

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
    @NotBlank private String colorName; // 'BLACK','WHITE','GRAY','NAVY','BEIGE','RED','PINK','ORANGE','YELLOW','GREEN','KHAKI','MINT','BLUE','SKYBLUE','PURPLE','BROWN','IVORY','CHARCOAL','DENIM'
    @NotNull private Integer originPrice;
    @NotNull private Integer finalPrice;
    @NotNull private Integer menuSubId;
    private String materialInfo; // 제품 소재
    private String manufacturerName; // 제조자
    private String countryOfOrigin; // 제조국
    private String washCareInfo; // 세탁방법 및 주의사항
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate manufacturedYm; // 제조연월
    private String qualityGuaranteeInfo; // 품질보증기준s
    private String afterServiceContact; // A/S 책임자와 전화번호
    private String afterServiceManager; // (선택) A/S 책임자
    private String afterServicePhone; // (선택) A/S 전화번호

    private Integer productId; // 상품 등록 후 반환될 상품 ID
}
