package me._hanho.nextjs_shop.model;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SellerLike {
    private int sellerLikeId;
    private Timestamp createdAt;
    private int sellerNo;
    private int userNo;
}
