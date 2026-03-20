package me._hanho.nextjs_shop.buy;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ManageStockHoldCouponRequest {
    private Integer holdCouponId;
    private Integer holdId;
    private Integer userCouponId;
    private Boolean isAdd;
}
