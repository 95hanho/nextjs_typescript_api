package me._hanho.nextjs_shop.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

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
    private Date created_at;
}
