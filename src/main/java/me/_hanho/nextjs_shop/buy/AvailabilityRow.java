package me._hanho.nextjs_shop.buy;

import lombok.Data;

@Data
public class AvailabilityRow {
    private int productOptionId;
    private int available; // stock - activeHoldSum
}