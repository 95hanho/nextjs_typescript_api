package me._hanho.nextjs_shop.mypage.dto;

import java.math.BigDecimal;
import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MyOrderDetailResponse {
	
	private int orderId;
	private Timestamp orderDate;
	private BigDecimal sellerCouponDiscountTotal; // 각 상품쿠폰 할인값 총합
	private BigDecimal cartCouponDiscountTotal; // 공용쿠폰 할인액수
	private int shippingFee;
	private int usedMileage;
	private int remainingMileage;
	private int totalPrice;
	private String paymentMethod;

	// 주소
	private int addressId;
	private String addressName;
	private String recipientName;
	private String addressPhone;
	private String zonecode;
	private String address;
	private String addressDetail;
	private String memo;
}
