SELECT c.coupon_id, c.description, c.discount_type, c.discount_value,
			c.max_discount, c.minimum_order_before_amount, c.status, 
			c.is_stackable, c.is_product_restricted,
			c.amount, c.start_date, c.end_date, c.created_at, c.updated_at, c.issue_method,
			uc.user_coupon_id, uc.used, 
			ca.coupon_allowed_id, 
			s.seller_name
		FROM nextjs_shop_coupon c
		LEFT JOIN nextjs_shop_user_coupon uc ON c.coupon_id = uc.coupon_id AND uc.used = 0 AND uc.user_no = 1
		LEFT JOIN nextjs_shop_coupon_allowed ca ON ca.coupon_id = c.coupon_id AND ca.product_id = 56
		LEFT JOIN nextjs_shop_seller s ON s.seller_no = c.seller_no
		WHERE c.seller_no = (SELECT seller_no FROM nextjs_shop_product p WHERE p.product_id = 56)
			AND (
				c.is_product_restricted = 0
				OR ca.product_id IS NOT NULL
			);
			
			
SELECT c.coupon_id, c.description, c.discount_type, c.discount_value,
	c.max_discount, c.minimum_order_before_amount, c.status, 
	c.is_stackable, c.is_product_restricted,
	c.amount, c.start_date, c.end_date, c.created_at, c.updated_at, c.issue_method,
	uc.user_coupon_id, uc.used, 
	s.seller_name
FROM nextjs_shop_coupon c
LEFT JOIN nextjs_shop_user_coupon uc ON uc c.coupon_id = uc.coupon_id
LEFT JOIN nextjs_shop_seller s ON s.seller_no = c.seller_no
LEFT JOIN nextjs_shop_coupon_allowed ca ON ca.coupon_id = c.coupon_id AND (ca.product_id = 56 OR ca.product_id IS NULL)
WHERE c.seller_no = (SELECT seller_no FROM nextjs_shop_product p WHERE p.product_id = 56)
AND (
				c.is_product_restricted = 0
				OR ca.product_id IS NOT NULL
			);