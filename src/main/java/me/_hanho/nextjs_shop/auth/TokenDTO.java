package me._hanho.nextjs_shop.auth;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenDTO {
    private int tokenId;
    private String connectIp;
    private String connectAgent;
    private String refreshToken;
    private String beforeToken;
    private Timestamp createdAt;
    private String userId;
}
