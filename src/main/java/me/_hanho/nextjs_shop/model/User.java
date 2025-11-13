package me._hanho.nextjs_shop.model;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private String userId;
    private String password;
    private String name;
    private String zonecode;
    private String address;
    private String addressDetail;
    private String birthday;
    private String phone;
    private String email;
    private Timestamp createdAt;
    private int mileage;
    private int tall;
    private int weight;
    private boolean withdrawalStatus;
}
