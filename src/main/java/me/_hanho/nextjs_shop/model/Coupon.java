package me._hanho.nextjs_shop.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // getter/setter, toString, equals, hashCode 자동 생성
@NoArgsConstructor // 기본 생성자
@AllArgsConstructor // 전체 필드 생성자
public class Coupon {
    private int couponId;
    private String description;
    private String couponCode;
    private String discountType; // 'percentage','fixed_amount'
    private BigDecimal discountValue; 
    private BigDecimal maxDiscount;
    private BigDecimal minimumOrderBeforeAmount;
    private String status; // 'ACTIVE','SUSPENDED','DELETED'
    private boolean isStackable;
    private boolean isProductRestricted;
    private int amount;
    private Timestamp startDate;
    private Timestamp endDate;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private int sellerNo;
    private int adminNo;
    private boolean isDeleted;
    private String issueMethod; // 발급되는 정책('CLAIM','AUTO','MANUAL')
//    CLAIM: 고객이 쿠폰함에서 “다운로드” 버튼으로 받는 쿠폰
//    MANUAL: 판매자/관리자가 고객에게 “지급” (CS, 이벤트 당첨 등)
//    AUTO: 회원가입/첫구매/등급조건/생일쿠폰처럼 조건 만족 시 자동발급
    
}