package me._hanho.nextjs_shop.model;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductQna {
    private int product_qna_id;
    private String question;
    private String answer;
    private Timestamp created_at;
    private Timestamp res_created_at;
    private int product_qna_type_id;
    private int product_id;
}
