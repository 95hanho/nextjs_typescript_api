-- 제품 like 랜덤주기
UPDATE nextjs_shop_product
SET like_count = FLOOR(RAND() * 50001);