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
    private String discountType;
    private BigDecimal discountValue; // 'percentage','fixed_amount'
    private BigDecimal maxDiscount;
    private BigDecimal minimumOrderBeforeAmount;
    private String status; // 'ACTIVE','SUSPENDED','DELETED'
    private Boolean isStackable;
    private Boolean isProductRestricted;
    private int amount;
    private Timestamp startDate;
    private Timestamp endDate;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private int sellerNo;
    
}