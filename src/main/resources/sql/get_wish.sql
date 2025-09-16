SELECT w.wish_id, w.created_at, w.user_id, 
			p.product_id, p.name, p.price, p.view_count, p.wish_count,
			pi.product_image_id, pi.file_id, 
			f.file_name,  f.store_name, f.file_path, f.copyright, f.copyright_url,
			s.seller_id, s.seller_name
		FROM nextjs_shop_wish w
		JOIN nextjs_shop_product p ON w.product_id = p.product_id
		JOIN nextjs_shop_seller s ON s.seller_id = p.seller_id
		LEFT JOIN nextjs_shop_product_image pi ON p.product_id = pi.product_id AND pi.thumbnail_status = 1
		LEFT JOIN nextjs_shop_file f ON pi.file_id = f.file_id
		WHERE w.user_id = 'hoseongs';