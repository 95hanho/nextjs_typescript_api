package me._hanho.nextjs_shop.product.dto;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductQnaResponse {
    private int productQnaId;
    private String question;
    private Timestamp createdAt;
    private String answer;
    private Timestamp resCreatedAt;
    private boolean answerRead;
    private boolean secret;
    private int productQnaTypeId;
    private String qnaTypeCode;
    private String qnaTypeName;
    private int productId;
    private Integer userNo;
    private String userName;
}
