package me._hanho.nextjs_shop.buy.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ManageStockHoldCouponRequest {
    private List<ManageStockHoldCoupon> holdCoupons;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class ManageStockHoldCoupon {
    private Integer holdId;
    private Integer userCouponId;
}