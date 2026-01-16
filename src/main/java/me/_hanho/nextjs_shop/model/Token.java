package me._hanho.nextjs_shop.model;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Token {
    private int tokenId;
    private String connectIp;
    private String connectAgent;
    private String refreshToken;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private int userNo;
    private int sellerNo;
    private int adminNo;
}
