package me._hanho.nextjs_shop.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryInfo {
    private int delivery_info_id;
    private int order_id;
    private String receiver_name;
    private String phone;
    private String zonecode;
    private String address;
    private String address_detail;
    private String memo;
    private Date ordered_date;
    private Date shipping_date;
    private Date delivered_date;
}
