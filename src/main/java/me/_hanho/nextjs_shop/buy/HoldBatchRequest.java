package me._hanho.nextjs_shop.buy;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
class HoldBatchRequest {
    private List<Integer> holdIds;
}