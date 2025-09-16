package me._hanho.nextjs_shop.model;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Seller {
    private String seller_id;
    private String password;
    private String business_registration_number;
    private String extension_number;
    private String mobile_number;
    private String email;
    private Timestamp created_at;
}
