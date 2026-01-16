package me._hanho.nextjs_shop.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginResponse {
	private int userNo;
    private String userId;
    private String password;
}
