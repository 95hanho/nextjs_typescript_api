package me._hanho.nextjs_shop.seller;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SellerRegisterRequest {
    @NotBlank private String sellerId; // 아이디
    @NotBlank private String password; // 비밀번호
    @NotBlank private String sellerName; // 판매자 이름(한글)
    @NotBlank private String sellerNameEn; // 판매자 이름(영어)
    @NotBlank private String extensionNumber; // 내선전화
    @NotBlank private String mobileNumber; // 대표 번호
    @NotBlank private String email; // 이메일
    @NotNull private String businessRegistrationNumber; // 사업자 등록번호
    @NotNull private String telecomSalesNumber; // 통신 판매자 번호
    @NotNull private String representativeName; // 대표자 이름
    @NotNull private String businessZipcode; // 사업장 소재지 우편번호
    @NotNull private String businessAddress; // 사업장 소재지 주소
    @NotNull private String businessAddressDetail; // 사업장 소재지 상세주소 
    
}