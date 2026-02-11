package me._hanho.nextjs_shop.product;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetailResponse {
    private int productId;
    private String name;
    private String colorName;
    private int originPrice;
    private int finalPrice;
    private Timestamp createdAt;
    private int likeCount;
    private int viewCount;
    private int wishCount;
    private String sellerName;
    private int menuSubId;
    private String subMenuName;
    private String topMenuName;
    private String gender;
    private String materialInfo; // 제품 소재
    private String manufacturerName; // 제조자
    private String countryOfOrigin; // 제조국
    private String washCareInfo; // 세탁방법 및 주의사항
    private String manufacturedYm; // 제조연월
    private String qualityGuaranteeInfo; // 품질보증기준
    private String afterServiceContact; // A/S 책임자와 전화번호
    private String afterServiceManager; // (선택) A/S 책임자
    private String afterServicePhone; // (선택) A/S 전화번호
    private int baseShippingFee; // 기본 배송비
    private int freeShippingMinAmount; // 무료배송 최소 주문금액
    private int extraShippingFee; // 제주/도서산간 추가 배송비
    private String shippingType; // 출고 방식('IMMEDIATE','RESERVED')
    private LocalDate shippingDueDate; // 출고 예정일
    private String shippingNote; // 출고 관련 추가 안내 문구
    
    private List<ProductImageFile> productImageList;
}

