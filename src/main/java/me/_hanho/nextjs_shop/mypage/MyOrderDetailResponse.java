package me._hanho.nextjs_shop.mypage;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MyOrderDetailResponse {
	
	private int orderId;
	private Timestamp orderDate;
	private BigDecimal eachCouponDiscountTotal; // 각 상품쿠폰 할인값 총합
	private BigDecimal commonCouponDiscountTotal; // 공용쿠폰 할인액수
	private int shippingFee;
	private int usedMileague;
	private int remainingMileage;
	private int totalPrice;
	private String paymentMethod;
	private String paymentCode;
	private String status;
	private Timestamp shippingDate;
	private Timestamp deliveredDate;
	private Timestamp returnDate;

	// 사용한 공동 쿠폰 내역
	private int userCouponId;
	private int couponId;
	private String description;
	
	private String couponCode;
	private String discountType;
	private BigDecimal discountValue;
	private BigDecimal maxDiscount;
	private BigDecimal minimumOrderBeforeAmount;
	private boolean isStackable;
	
	// 주소
	private int addressId;
	private String addressName;
	private String recipientName;
	private String addressPhone;
	private String zonecode;
	private String address;
	private String addressDetail;
	private String memo;
	private boolean defaultAddress;
	
	private List<MyOrderDetailItem> items;

}
