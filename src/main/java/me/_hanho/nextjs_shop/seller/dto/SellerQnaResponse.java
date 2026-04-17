package me._hanho.nextjs_shop.seller.dto;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SellerQnaResponse {
    private int productQnaId;
    private String question;
    private Timestamp createdAt;
    private String answer;
    private Timestamp resCreatedAt;
    private boolean secret;
    private int productQnaTypeId;
    private String productQnaTypeName;
    private int productId;
    private String productName;
    private String userName;
}