package me._hanho.nextjs_shop.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductQna {
    private int product_qna_id;
    private String question;
    private String answer;
    private Date created_at;
    private Date res_created_at;
    private int product_qna_type_id;
    private int product_id;
}
