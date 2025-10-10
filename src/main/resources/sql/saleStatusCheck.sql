-- 가용수량: stock - sum(HOLD & 미만료(>NOW()))
SELECT
pd.product_detail_id AS productDetailId,
(pd.stock - IFNULL(SUM(
  CASE
    WHEN sh.status = 'HOLD' AND sh.expires_at > NOW() THEN sh.count
    ELSE 0
  END
), 0)) AS available
FROM nextjs_shop_product_detail pd
LEFT JOIN nextjs_shop_stock_hold sh
ON sh.product_detail_id = pd.product_detail_id
WHERE pd.product_detail_id IN (127, 126)
GROUP BY pd.product_detail_id;

-- 홀드 다건 생성: TTL=NOW()+#{ttlSeconds}초
INSERT INTO nextjs_shop_stock_hold
   (user_id, product_detail_id, count, status, expires_at, created_at)
 VALUES
   <foreach collection="rows" item="r" separator=",">
     (#{r.userId}, #{r.productDetailId}, #{r.count}, 'HOLD',
      DATE_ADD(NOW(), INTERVAL #{r.ttlSeconds} SECOND),
      NOW())
   </foreach>


