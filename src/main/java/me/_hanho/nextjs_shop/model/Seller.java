package me._hanho.nextjs_shop.model;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Seller {
    private String sellerId;
    private String password;
    private String sellerName;
    private String businessRegistrationNumber;
    private String extensionNumber;
    private String mobileNumber;
    private String email;
    private Timestamp createdAt;
}
