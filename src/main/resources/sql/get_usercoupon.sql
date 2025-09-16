SELECT uc.usercoupon_id, uc.used, 
			c.coupon_id, c.description, c.discount_type, c.discount_value,
			c.max_discount, c.minimum_order_before_amount, c.status, c.is_stackable, c.is_product_restricted,
			c.amount, c.start_date, c.end_date, c.created_at, c.updated_at, 
			c.seller_id, s.seller_name
		FROM nextjs_shop_usercoupon uc
		JOIN nextjs_shop_coupon c ON c.coupon_id = uc.coupon_id
		LEFT JOIN nextjs_shop_seller s ON s.seller_id = c.seller_id
		WHERE uc.user_id = 'hoseongs';
