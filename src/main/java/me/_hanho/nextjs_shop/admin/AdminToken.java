package me._hanho.nextjs_shop.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminToken {
    private String connectIp;
    private String connectAgent;
    private String refreshToken;
    private int adminNo;
}
