package me._hanho.nextjs_shop.auth;

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
}
