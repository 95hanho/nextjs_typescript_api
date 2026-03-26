package me._hanho.nextjs_shop.buy;

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
	private Integer usedMileage;
	private String paymentMethod;
	private List<Integer> holdIds;
}
