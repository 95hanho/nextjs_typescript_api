package me._hanho.nextjs_shop.seller;

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
public class AddCouponRequest {
    @NotBlank private String description;
    private String couponCode;
    @NotBlank private String discountType; // 'percentage','fixed_amount'
    @NotNull private BigDecimal discountValue;
    @NotNull private BigDecimal maxDiscount;
    private BigDecimal minimumOrderBeforeAmount;
    @NotNull
    @Positive
    private Integer amount;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime startDate;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime endDate;
}