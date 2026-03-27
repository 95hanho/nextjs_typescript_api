package me._hanho.nextjs_shop.buy.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.Builder;
import lombok.Getter;
import me._hanho.nextjs_shop.model.OrderItem;

@Getter
@Builder
public class PaymentPrepareResult {
    private BigDecimal sellerCouponDiscountTotal;
    private BigDecimal cartCouponDiscountTotal;
    private BigDecimal shippingFee;
    private BigDecimal totalPrice;
    private List<OrderItem> orderItems;
    private List<OrderItemCouponWithHoldId> orderItemCoupons;
}
