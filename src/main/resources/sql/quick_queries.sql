-- 제품 like 랜덤주기
UPDATE nextjs_shop_product
SET like_count = FLOOR(RAND() * 50001);

-- 
SELECT c.cart_id, c.created_at, c.product_detail_id, c.user_id,
			pd.add_price, pd.stock, pd.size, p.product_id, 
			p.name AS productName, p.price,
			w.wish_id,
			f.file_id, f.file_name, f.store_name, f.file_path, f.copyright, f.copyright_url,
			s.seller_id, s.seller_name,
			c.quantity, c.selected
		FROM nextjs_shop_cart c
		JOIN nextjs_shop_product_detail pd ON pd.product_detail_id = c.product_detail_id
		JOIN nextjs_shop_product p ON p.product_id = pd.product_id
		LEFT JOIN nextjs_shop_wish w ON w.product_id = p.product_id AND w.user_id = c.user_id
		JOIN nextjs_shop_seller s ON s.seller_id = p.seller_id
		LEFT JOIN (
	        SELECT 
	            pi_inner.product_id,
	            pi_inner.file_id
	        FROM nextjs_shop_product_image pi_inner
	        INNER JOIN (
	            SELECT 
	                product_id,
	                MIN(sort_key) AS min_sort_key
	            FROM nextjs_shop_product_image
	            GROUP BY product_id
	        ) first_img
	            ON pi_inner.product_id = first_img.product_id
	           AND pi_inner.sort_key   = first_img.min_sort_key
	    ) tpi ON tpi.product_id = p.product_id
	    LEFT JOIN nextjs_shop_file f ON f.file_id = tpi.file_id
		WHERE c.user_id = 'hoseongs';