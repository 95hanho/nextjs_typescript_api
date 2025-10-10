package me._hanho.nextjs_shop.buy;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PayUserDTO {
    private String user_id;
    private String zonecode;
    private String address;
    private String address_detail;
    private String phone;
    private String email;
    private int mileage;
    private int tall;
    private int weight;
}
