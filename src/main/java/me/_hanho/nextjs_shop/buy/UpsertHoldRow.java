package me._hanho.nextjs_shop.buy;

@lombok.Data @lombok.AllArgsConstructor
public class UpsertHoldRow {
    private Integer userNo;
    private Integer productOptionId;
    private Integer cartId;
    private Integer count;
    private Integer ttlSeconds;
    private String returnUrl;
}