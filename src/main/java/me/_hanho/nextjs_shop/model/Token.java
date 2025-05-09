package me._hanho.nextjs_shop.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Token {
    private int token_id;
    private String connect_ip;
    private String connect_agent;
    private String refresh_token;
    private Date created_at;
    private String user_id;
}
