package me._hanho.nextjs_shop.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private String user_id;
    private String password;
    private String name;
    private String zonecode;
    private String address;
    private String address_detail;
    private String birthday;
    private String phone;
    private String email;
    private Date created_at;
    private int mileage;
    private int tall;
    private int weight;
    private int withdrawal_status;
}
