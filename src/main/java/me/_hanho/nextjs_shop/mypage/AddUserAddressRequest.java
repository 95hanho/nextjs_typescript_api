package me._hanho.nextjs_shop.mypage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddUserAddressRequest {
	private String addressName;
	private String recipientName;
	private String addressPhone;
	private String zonecode;
	private String address;
	private String addressDetail;
	private String memo;
	private boolean defaultAddress;
	private Integer userNo;
}
