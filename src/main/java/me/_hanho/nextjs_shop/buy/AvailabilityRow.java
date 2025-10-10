package me._hanho.nextjs_shop.buy;

import lombok.Data;

@Data
public class AvailabilityRow {
    private int productDetailId;
    private int available; // stock - activeHoldSum
}