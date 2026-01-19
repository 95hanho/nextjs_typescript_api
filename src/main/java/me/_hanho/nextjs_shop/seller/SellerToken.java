package me._hanho.nextjs_shop.seller;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SellerToken {
    private String connectIp;
    private String connectAgent;
    private String refreshToken;
    private int sellerNo;
}
