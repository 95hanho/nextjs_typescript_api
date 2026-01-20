package me._hanho.nextjs_shop.seller;

import java.sql.Timestamp;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me._hanho.nextjs_shop.model.ProductOption;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SellerProductResponse {
	private int productId;
	private String name;
	private String colorName;
	private int originPrice;
	private int finalPrice;
	private Timestamp createdAt;
	private int viewCount;
	private int wishCount;
	
	private boolean saleStop; // 판매 중지여부
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
	
	private List<ProductOption> optionList;
	
}
