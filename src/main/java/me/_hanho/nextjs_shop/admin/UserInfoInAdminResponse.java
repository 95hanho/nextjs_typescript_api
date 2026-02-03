package me._hanho.nextjs_shop.admin;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoInAdminResponse {
	private int userNo;
    private String userId;
    private String name;
    private String zonecode;
    private String address;
    private String addressDetail;
    private Timestamp birthday;
    private String phone;
    private String email;
    private Timestamp createdAt;
    private int mileage;
    private String withdrawalStatus; // 'ACTIVE','REQUESTED','WITHDRAWN'
    private Timestamp withdrawalRequestedAt;
    private Timestamp withdrawalCompletedAt;
}
