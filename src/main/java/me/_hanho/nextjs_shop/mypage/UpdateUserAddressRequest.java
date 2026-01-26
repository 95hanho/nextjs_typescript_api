package me._hanho.nextjs_shop.mypage;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserAddressRequest {
	@NotNull private Integer addressId;
	@NotBlank private String addressName;
	@NotBlank private String recipientName;
	@NotBlank private String addressPhone;
	@NotBlank private String zonecode;
	@NotBlank private String address;
	@NotBlank private String addressDetail;
	@NotBlank private String memo;
	@NotNull private Boolean defaultAddress;
}