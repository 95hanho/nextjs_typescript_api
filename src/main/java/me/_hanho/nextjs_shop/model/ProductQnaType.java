package me._hanho.nextjs_shop.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductQnaType {
    private int productQnaTypeId;
    private String code; // 'ALL', 'PRODUCT','RESTOCK','SIZE','SHIPPING','ETC'
    private String name;
}
