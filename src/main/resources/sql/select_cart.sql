SELECT c.cart_id, c.created_at, c.product_option_id, pd.add_price, pd.stock, pd.size, p.product_id, 
	p.name AS product_name, p.price,
	f.file_id, f.file_name, f.store_name, f.file_path, f.copyright, f.copyright_url,
	s.seller_id, s.seller_name,
	c.quantity, c.selected
FROM nextjs_shop_cart c
JOIN nextjs_shop_product_option pd ON pd.product_option_id = c.product_option_id
JOIN nextjs_shop_product p ON p.product_id = pd.product_id
JOIN nextjs_shop_seller s ON s.seller_id = p.seller_id
LEFT JOIN (
    SELECT pi.product_id, pi.file_id
    FROM nextjs_shop_product_image pi
    WHERE pi.thumbnail_status = 1
    GROUP BY pi.product_id
) pi ON pi.product_id = p.product_id
LEFT JOIN nextjs_shop_file f ON f.file_id = pi.file_id
WHERE c.user_id = 'hoseongs';