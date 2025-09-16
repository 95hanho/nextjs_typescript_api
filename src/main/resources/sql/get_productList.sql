-- 조회 orderby + 페이징처리 적용 전
SELECT p.product_id, p.name, p.color_name, p.price, p.created_at, p.view_count, p.wish_count, p.seller_id, 
	s.seller_name,
	f.file_id, f.file_name, f.store_name, f.file_path, f.copyright, f.copyright_url
FROM nextjs_shop_product p
JOIN nextjs_shop_seller s ON s.seller_id = p.seller_id
LEFT JOIN nextjs_shop_product_image pi ON pi.product_id = p.product_id
LEFT JOIN nextjs_shop_file f ON f.file_id = pi.file_id
WHERE p.sale_stop = 0 AND p.menu_sub_id = 25
ORDER BY p.product_id DESC;

-- 최신순 처음 조회
SELECT
	    p.product_id, p.name, p.color_name, p.price, p.created_at,
	    p.view_count, p.wish_count, p.seller_id,
	    s.seller_name,
	    f.file_id, f.file_name, f.store_name, f.file_path, f.copyright, f.copyright_url
	  FROM nextjs_shop_product p
	  JOIN nextjs_shop_seller s ON s.seller_id = p.seller_id
	  LEFT JOIN (
	    SELECT pi.product_id, MIN(pi.file_id) AS file_id
	    FROM nextjs_shop_product_image pi
	    WHERE pi.thumbnail_status = 1
	    GROUP BY pi.product_id
	  ) tpi ON tpi.product_id = p.product_id
	  LEFT JOIN nextjs_shop_file f ON f.file_id = tpi.file_id
	  WHERE p.sale_stop = 0
	    AND p.menu_sub_id = 25
	      ORDER BY p.created_at DESC, p.product_id DESC
	  LIMIT 30;
-- 최신순 다음페이지 예시
SELECT
	    p.product_id, p.name, p.color_name, p.price, p.created_at,
	    p.view_count, p.wish_count, p.seller_id,
	    s.seller_name,
	    f.file_id, f.file_name, f.store_name, f.file_path, f.copyright, f.copyright_url
	  FROM nextjs_shop_product p
	  JOIN nextjs_shop_seller s ON s.seller_id = p.seller_id
	  LEFT JOIN (
	    SELECT pi.product_id, MIN(pi.file_id) AS file_id
	    FROM nextjs_shop_product_image pi
	    WHERE pi.thumbnail_status = 1
	    GROUP BY pi.product_id
	  ) tpi ON tpi.product_id = p.product_id
	  LEFT JOIN nextjs_shop_file f ON f.file_id = tpi.file_id
	  WHERE p.sale_stop = 0
	    AND p.menu_sub_id = 25
	    AND (p.created_at, p.product_id) < ('2025-09-15 16:06:32', 325)
	      ORDER BY p.created_at DESC, p.product_id DESC
	  LIMIT 30;
-- 최신순 마지막페이지 예시
SELECT
	    p.product_id, p.name, p.color_name, p.price, p.created_at,
	    p.view_count, p.wish_count, p.seller_id,
	    s.seller_name,
	    f.file_id, f.file_name, f.store_name, f.file_path, f.copyright, f.copyright_url
	  FROM nextjs_shop_product p
	  JOIN nextjs_shop_seller s ON s.seller_id = p.seller_id
	  LEFT JOIN (
	    SELECT pi.product_id, MIN(pi.file_id) AS file_id
	    FROM nextjs_shop_product_image pi
	    WHERE pi.thumbnail_status = 1
	    GROUP BY pi.product_id
	  ) tpi ON tpi.product_id = p.product_id
	  LEFT JOIN nextjs_shop_file f ON f.file_id = tpi.file_id
	  WHERE p.sale_stop = 0
	    AND p.menu_sub_id = 25
	    AND (p.created_at, p.product_id) < ('2025-09-15 16:06:32', 58)
	      ORDER BY p.created_at DESC, p.product_id DESC
	  LIMIT 30;
	  
-- 인기순 처음 조회
SELECT
 p.product_id, p.name, p.color_name, p.price, p.created_at,
 p.view_count, p.wish_count, p.seller_id,
 s.seller_name,
 f.file_id, f.file_name, f.store_name, f.file_path, f.copyright, f.copyright_url
FROM nextjs_shop_product p
JOIN nextjs_shop_seller s ON s.seller_id = p.seller_id
LEFT JOIN (
 SELECT pi.product_id, MIN(pi.file_id) AS file_id
 FROM nextjs_shop_product_image pi
 WHERE pi.thumbnail_status = 1
 GROUP BY pi.product_id
) tpi ON tpi.product_id = p.product_id
LEFT JOIN nextjs_shop_file f ON f.file_id = tpi.file_id
WHERE p.sale_stop = 0
 AND p.menu_sub_id = 25
   ORDER BY (p.view_count + p.wish_count) DESC,
            p.created_at DESC,
            p.product_id DESC
LIMIT 30;
-- 인기순 다음페이지 조회 예시
SELECT
 p.product_id, p.name, p.color_name, p.price, p.created_at,
 p.view_count, p.wish_count, p.seller_id,
 s.seller_name,
 f.file_id, f.file_name, f.store_name, f.file_path, f.copyright, f.copyright_url
FROM nextjs_shop_product p
JOIN nextjs_shop_seller s ON s.seller_id = p.seller_id
LEFT JOIN (
 SELECT pi.product_id, MIN(pi.file_id) AS file_id
 FROM nextjs_shop_product_image pi
 WHERE pi.thumbnail_status = 1
 GROUP BY pi.product_id
) tpi ON tpi.product_id = p.product_id
LEFT JOIN nextjs_shop_file f ON f.file_id = tpi.file_id
WHERE p.sale_stop = 0
 AND p.menu_sub_id = 25
     AND (
       (p.view_count + p.wish_count) < 46083
       OR (
         (p.view_count + p.wish_count) = 46083
         -- 인기도가 동률인 것도 나오게 하기위해서 AND (p.created_at, p.product_id) < (#{lastCreatedAt}, #{lastProductId}) 가 붙음.
         -- 가장 마지막으로 뜨는 행의 created_at과 product_id와 popularity넣으면됨,
         AND (p.created_at, p.product_id) < ('2025-09-15 16:06:32', 219)
       )
     )
   ORDER BY (p.view_count + p.wish_count) DESC,
            p.created_at DESC,
            p.product_id DESC
LIMIT 30;

--
UPDATE nextjs_shop_product
SET
  view_count = FLOOR(RAND() * 50000),  -- 0 ~ 49,999
  wish_count = FLOOR(RAND() * 1000);   -- 0 ~ 999