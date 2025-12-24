package me._hanho.nextjs_shop.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PhoneAuth {
	private int phoneAuthId;
	private String userId;
	private String phoneAuthToken;
	private String phone;
	private String verificationCode;
	private String connectIp;
	private String connectAgent;
	private boolean isUsed;
}
