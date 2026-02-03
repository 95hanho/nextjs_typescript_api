package me._hanho.nextjs_shop.auth;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoResponse {
    private String name;
    private String zonecode;
    private String address;
    private String addressDetail;
    private Timestamp birthday;
    private String phone;
    private String email;
    private Timestamp createdAt;
    private int mileage;
    private int tall;
    private int weight;
}
