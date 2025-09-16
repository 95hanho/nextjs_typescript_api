package me._hanho.nextjs_shop.model;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private Timestamp ordered_date;
    private Timestamp shipping_date;
    private Timestamp delivered_date;
}
