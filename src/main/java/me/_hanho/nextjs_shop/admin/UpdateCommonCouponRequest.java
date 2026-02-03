package me._hanho.nextjs_shop.admin;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // getter/setter, toString, equals, hashCode 자동 생성
@NoArgsConstructor // 기본 생성자
@AllArgsConstructor // 전체 필드 생성자
public class UpdateCommonCouponRequest {
	@NotNull private Integer couponId;
    @NotBlank private String description;
    @NotBlank private String discountType; // 'percentage','fixed_amount' -> 변경은 불가하게
    @NotNull private BigDecimal discountValue;
    @NotNull private BigDecimal maxDiscount;
    private BigDecimal minimumOrderBeforeAmount;
    @NotBlank private String status; // 'ACTIVE','SUSPENDED','DELETED'
    @NotNull private Boolean isStackable;
    @NotNull private Boolean isProductRestricted;
    @NotNull
    @Positive
    private Integer amount;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime startDate;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime endDate;
    
}