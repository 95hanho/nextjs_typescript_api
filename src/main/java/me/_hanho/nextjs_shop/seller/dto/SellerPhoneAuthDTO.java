package me._hanho.nextjs_shop.seller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SellerPhoneAuthDTO {
	private String phoneAuthToken;
	private String phone;
	private String verificationCode;
	private String mode; // 'REGISTRATION'
	private String connectIp;
	private String connectAgent;
}
