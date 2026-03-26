package me._hanho.nextjs_shop.buy;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryInfoBySeller {
    private int totalFinalPrice = 0;
    private int baseShippingFee = 0;
    private int freeShippingMinAmount = 0;
}
