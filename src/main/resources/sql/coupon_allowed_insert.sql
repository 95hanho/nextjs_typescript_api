/* =======================================
   2) 쿠폰-허용상품 매핑 INSERT (예시)
   - 쿠폰코드 기준으로 coupon_id를 찾아 매핑
   ======================================= */

-- 플랫폼 10%: 다양한 인기상품 3개 예시
INSERT INTO nextjs_shop_coupon_allowed (coupon_id, product_id)
SELECT c.coupon_id, p.product_id
FROM nextjs_shop_coupon c
JOIN (SELECT 21 AS product_id UNION ALL SELECT 24 UNION ALL SELECT 35) p
WHERE c.coupon_code = 'PLATFORM10P';

-- 플랫폼 3K: 또 다른 3개 예시
INSERT INTO nextjs_shop_coupon_allowed (coupon_id, product_id)
SELECT c.coupon_id, p.product_id
FROM nextjs_shop_coupon c
JOIN (SELECT 12 AS product_id UNION ALL SELECT 28 UNION ALL SELECT 34) p
WHERE c.coupon_code = 'PLATFORM-3K';

-- seller01 (Urban Style): 해당 셀러 상품
-- 상품: 11, 41, 26, 1 (모두 seller01)
INSERT INTO nextjs_shop_coupon_allowed (coupon_id, product_id)
SELECT c.coupon_id, p.product_id
FROM nextjs_shop_coupon c
JOIN (SELECT 11 AS product_id UNION ALL SELECT 41 UNION ALL SELECT 26 UNION ALL SELECT 1) p
WHERE c.coupon_code = 'URBAN15P';

INSERT INTO nextjs_shop_coupon_allowed (coupon_id, product_id)
SELECT c.coupon_id, p.product_id
FROM nextjs_shop_coupon c
JOIN (SELECT 11 AS product_id UNION ALL SELECT 41 UNION ALL SELECT 26 UNION ALL SELECT 1) p
WHERE c.coupon_code = 'URBAN-5K';

-- seller02 (Daily Wear): 2, 12, 27, 42
INSERT INTO nextjs_shop_coupon_allowed (coupon_id, product_id)
SELECT c.coupon_id, p.product_id
FROM nextjs_shop_coupon c
JOIN (SELECT 2 AS product_id UNION ALL SELECT 12 UNION ALL SELECT 27 UNION ALL SELECT 42) p
WHERE c.coupon_code = 'DAILY-10K';

-- seller03 (Trend Factory): 43, 28, 13, 3
INSERT INTO nextjs_shop_coupon_allowed (coupon_id, product_id)
SELECT c.coupon_id, p.product_id
FROM nextjs_shop_coupon c
JOIN (SELECT 43 AS product_id UNION ALL SELECT 28 UNION ALL SELECT 13 UNION ALL SELECT 3) p
WHERE c.coupon_code = 'TREND20P';

-- seller04 (Blue Ocean): 44, 14, 29, 4
INSERT INTO nextjs_shop_coupon_allowed (coupon_id, product_id)
SELECT c.coupon_id, p.product_id
FROM nextjs_shop_coupon c
JOIN (SELECT 44 AS product_id UNION ALL SELECT 14 UNION ALL SELECT 29 UNION ALL SELECT 4) p
WHERE c.coupon_code = 'BLUE-7K';

-- seller06 (Sporty Fit): 16, 46, 6
INSERT INTO nextjs_shop_coupon_allowed (coupon_id, product_id)
SELECT c.coupon_id, p.product_id
FROM nextjs_shop_coupon c
JOIN (SELECT 16 AS product_id UNION ALL SELECT 46 UNION ALL SELECT 6) p
WHERE c.coupon_code = 'SPORTY10P';

-- seller07 (Casual Mood): 32, 17, 47, 7
INSERT INTO nextjs_shop_coupon_allowed (coupon_id, product_id)
SELECT c.coupon_id, p.product_id
FROM nextjs_shop_coupon c
JOIN (SELECT 32 AS product_id UNION ALL SELECT 17 UNION ALL SELECT 47 UNION ALL SELECT 7) p
WHERE c.coupon_code = 'CASUAL-5K';

-- seller09 (Luxury Point): 9, 34, 49, 19
INSERT INTO nextjs_shop_coupon_allowed (coupon_id, product_id)
SELECT c.coupon_id, p.product_id
FROM nextjs_shop_coupon c
JOIN (SELECT 9 AS product_id UNION ALL SELECT 34 UNION ALL SELECT 49 UNION ALL SELECT 19) p
WHERE c.coupon_code = 'LUXE-20K';

-- seller12 (Summer Breeze): 22, 37
INSERT INTO nextjs_shop_coupon_allowed (coupon_id, product_id)
SELECT c.coupon_id, p.product_id
FROM nextjs_shop_coupon c
JOIN (SELECT 22 AS product_id UNION ALL SELECT 37) p
WHERE c.coupon_code = 'SUMMER-12P';

-- seller13 (Winter Warm, 비활성 쿠폰이지만 매핑 예시): 38, 23
INSERT INTO nextjs_shop_coupon_allowed (coupon_id, product_id)
SELECT c.coupon_id, p.product_id
FROM nextjs_shop_coupon c
JOIN (SELECT 38 AS product_id UNION ALL SELECT 23) p
WHERE c.coupon_code = 'WINTER-15P';