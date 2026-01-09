SELECT p.product_id, p.name, p.color_name, p.price, p.created_at, p.view_count, p.wish_count, p.seller_id,
			ms.menu_name AS sub_menu_name, mt.menu_name AS top_menu_name, mt.gender
		FROM nextjs_shop_product p
		JOIN nextjs_shop_menu_sub ms ON p.menu_sub_id = ms.menu_sub_id
		JOIN nextjs_shop_menu_top mt ON mt.menu_top_id = ms.menu_top_id
		WHERE p.seller_id = 'seller01'
		
SELECT product_option_id, product_id, add_price, stock, created_at, size, sales_count
		FROM nextjs_shop_product_option
		WHERE product_id IN (1, 2)
		ORDER BY product_id, size;
		
		
SELECT *
		FROM nextjs_shop_coupon
		WHERE seller_id = 'seller01' OR seller_id IS NULL;
		
		
SELECT pv.product_view_id, pv.view_date, pv.user_id, pv.product_id
FROM nextjs_shop_product_view pv
JOIN nextjs_shop_product p ON p.product_id = pv.product_id
WHERE p.seller_id = 'seller01';
		
		
UPDATE nextjs_shop_cart
SET selected = 1;

-- 1) user_id 외래키 잠시 제거 (이름이 다르면 SHOW CREATE 결과의 이름을 사용)
ALTER TABLE nextjs_shop_stock_hold
  DROP FOREIGN KEY FK1_stock_hold_user;

-- 2) status 기반 유니크 인덱스 제거
ALTER TABLE nextjs_shop_stock_hold
  DROP INDEX UQ_user_pd_status;

-- 3) active_hold 기반 유니크 인덱스 생성 (활성 홀드만 1건 보장)
ALTER TABLE nextjs_shop_stock_hold
  ADD UNIQUE INDEX UQ_user_pd_active (user_id, product_option_id, active_hold);

-- 4) user_id 외래키 복구
ALTER TABLE nextjs_shop_stock_hold
  ADD CONSTRAINT FK1_stock_hold_user
  FOREIGN KEY (user_id)
  REFERENCES nextjs_shop_user (user_id)
  ON UPDATE NO ACTION
  ON DELETE NO ACTION;


--
UPDATE nextjs_shop_stock_hold
    SET expires_at = DATE_ADD(NOW(), INTERVAL 180 SECOND)
    WHERE status = 'HOLD'
      AND active_hold = 1
      AND hold_id IN (13, 14);
      
      
UPDATE nextjs_shop_user_address
SET usedate_at = NULL
WHERE usedate_at IS NOT NULL;



SELECT ol.order_list_id AS orderListId, ol.order_id AS orderId, ol.count, ol.order_price AS orderPrice, ol.discount_price AS discountPrice,
	ol.final_price AS finalPrice,
	sh.hold_id AS holdId,
	pd.product_option_id, pd.add_price, pd.size,
	p.product_id, p.name AS productName, p.color_name, p.price
FROM nextjs_shop_order_list ol
JOIN nextjs_shop_stock_hold sh ON sh.hold_id = ol.hold_id
JOIN nextjs_shop_product_option pd ON pd.product_option_id = sh.product_option_id
JOIN nextjs_shop_product p ON p.product_id = pd.product_id
WHERE ol.order_id = 30;

      