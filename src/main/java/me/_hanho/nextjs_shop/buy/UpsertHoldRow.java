package me._hanho.nextjs_shop.buy;

@lombok.Data @lombok.AllArgsConstructor
public class UpsertHoldRow {
    private String userId;
    private int productDetailId;
    private int count;
    private int ttlSeconds;
}