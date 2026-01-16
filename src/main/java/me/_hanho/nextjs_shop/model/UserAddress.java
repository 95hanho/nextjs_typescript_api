package me._hanho.nextjs_shop.model;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserAddress {
	private int addressId;
	private String addressName;
	private String recipientName;
	private String addressPhone;
	private String zonecode;
	private String address;
	private String addressDetail;
	private String memo;
	private boolean defaultAddress;
	private Timestamp createdAt;
	private Timestamp updatedAt;
	private Timestamp usedateAt;
	private int userNo;
	private boolean isDeleted;
}
