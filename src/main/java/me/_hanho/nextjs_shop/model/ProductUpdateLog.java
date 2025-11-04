package me._hanho.nextjs_shop.model;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductUpdateLog {
    private int productUpdateLogId;
    private int productId;
    private String sellerId;
    private Timestamp updatedAt;
    private String updatedDetail;
}
