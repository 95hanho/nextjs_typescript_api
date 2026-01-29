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
public class PhoneAuth {
	private int phoneAuthId;
	private int userNo;
	private String phoneAuthToken;
	private String phone;
	private String verificationCode;
	private String mode; // 'JOIN','IDFIND','PWDFIND','CHANGE'
	private String connectIp;
	private String connectAgent;
	private boolean isUsed;
	private Timestamp createdAt;
}
