SELECT *
FROM nextjs_shop_coupon c
LEFT JOIN nextjs_shop_coupon_allowed ca ON ca.coupon_id = c.coupon_id
WHERE ca.product_id IN (2, 12) OR seller_id is NULL;