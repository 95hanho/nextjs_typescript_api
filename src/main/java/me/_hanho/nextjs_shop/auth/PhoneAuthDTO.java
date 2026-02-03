package me._hanho.nextjs_shop.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PhoneAuthDTO {
	private Integer userNo;
	private String phoneAuthToken;
	private String phone;
	private String verificationCode;
	private String mode; // 'JOIN','IDFIND','PWDFIND','CHANGE'
	private String connectIp;
	private String connectAgent;
}
