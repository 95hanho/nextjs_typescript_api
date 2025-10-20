package me._hanho.nextjs_shop.model;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserAddress {
	private int address_id;
	private String address_name;
	private String address_phone;
	private String zonecode;
	private String address;
	private String address_detail;
	private String memo;
	private boolean default_address;
	private Timestamp created_at;
	private Timestamp usedate_at;
	private String user_id;
}
