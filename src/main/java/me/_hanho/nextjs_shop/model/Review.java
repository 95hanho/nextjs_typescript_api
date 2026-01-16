package me._hanho.nextjs_shop.model;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Review {
    private int reviewId;
    private String content;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private int rating;
    private int orderListId;
    private int productId;
    private int productOptionId;
    private int userNo;
    private boolean isdeleted;
}
