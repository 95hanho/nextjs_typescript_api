package me._hanho.nextjs_shop.buy.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PayRequest {
	
	private ShippingAddressRequest shippingAddress;
	private Boolean setAsDefault;
	private int usedMileage;
	private String paymentMethod;
	private List<Integer> holdIds;
}
