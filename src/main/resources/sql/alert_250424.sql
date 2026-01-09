ALTER TABLE `nextjs_shop_product_option_category` ADD CONSTRAINT `FK_nextjs_shopproduct_option_TO_product_option_category_1` FOREIGN KEY (
	`product_option_id`
)
REFERENCES `nextjs_shop_product_option` (
	`product_option_id`
);

ALTER TABLE `nextjs_shop_coupon` ADD CONSTRAINT `FK_nextjs_shopuser_TO_coupon_1` FOREIGN KEY (
	`user_id`
)
REFERENCES `nextjs_shop_user` (
	`user_id`
);

ALTER TABLE `nextjs_shop_wish` ADD CONSTRAINT `FK_nextjs_shopproduct_TO_wish_1` FOREIGN KEY (
	`product_id`
)
REFERENCES `nextjs_shop_product` (
	`product_id`
);

ALTER TABLE `nextjs_shop_wish` ADD CONSTRAINT `FK_nextjs_shopuser_TO_wish_1` FOREIGN KEY (
	`user_id`
)
REFERENCES `nextjs_shop_user` (
	`user_id`
);

ALTER TABLE `nextjs_shop_order_group` ADD CONSTRAINT `FK_nextjs_shopuser_TO_order_group_1` FOREIGN KEY (
	`user_id`
)
REFERENCES `nextjs_shop_user` (
	`user_id`
);

ALTER TABLE `nextjs_shop_product` ADD CONSTRAINT `FK_nextjs_shopseller_TO_product_1` FOREIGN KEY (
	`seller_id`
)
REFERENCES `nextjs_shop_seller` (
	`seller_id`
);

ALTER TABLE `nextjs_shop_product` ADD CONSTRAINT `FK_nextjs_shopmenu_sub_TO_product_1` FOREIGN KEY (
	`menu_sub_id`
)
REFERENCES `nextjs_shop_menu_sub` (
	`menu_sub_id`
);

ALTER TABLE `nextjs_shop_menu_sub` ADD CONSTRAINT `FK_nextjs_shopmenu_top_TO_menu_sub_1` FOREIGN KEY (
	`menu_top_id`
)
REFERENCES `nextjs_shop_menu_top` (
	`menu_top_id`
);

ALTER TABLE `nextjs_shop_product_qna` ADD CONSTRAINT `FK_nextjs_shopproduct_qna_type_TO_product_qna_1` FOREIGN KEY (
	`product_qna_type_id`
)
REFERENCES `nextjs_shop_product_qna_type` (
	`product_qna_type_id`
);

ALTER TABLE `nextjs_shop_product_qna` ADD CONSTRAINT `FK_nextjs_shopproduct_TO_product_qna_1` FOREIGN KEY (
	`product_id`
)
REFERENCES `nextjs_shop_product` (
	`product_id`
);

ALTER TABLE `nextjs_shop_product_update_log` ADD CONSTRAINT `FK_nextjs_shopproduct_TO_product_update_log_1` FOREIGN KEY (
	`product_id`
)
REFERENCES `nextjs_shop_product` (
	`product_id`
);

ALTER TABLE `nextjs_shop_product_update_log` ADD CONSTRAINT `FK_nextjs_shopseller_TO_product_update_log_1` FOREIGN KEY (
	`seller_id`
)
REFERENCES `nextjs_shop_seller` (
	`seller_id`
);

ALTER TABLE `nextjs_shop_product_image` ADD CONSTRAINT `FK_nextjs_shopproduct_TO_product_image_1` FOREIGN KEY (
	`product_id`
)
REFERENCES `nextjs_shop_product` (
	`product_id`
);

ALTER TABLE `nextjs_shop_product_image` ADD CONSTRAINT `FK_nextjs_shopfile_TO_product_image_1` FOREIGN KEY (
	`file_id`
)
REFERENCES `nextjs_shop_file` (
	`file_id`
);

ALTER TABLE `nextjs_shop_product_option` ADD CONSTRAINT `FK_nextjs_shopproduct_TO_product_option_1` FOREIGN KEY (
	`product_id`
)
REFERENCES `nextjs_shop_product` (
	`product_id`
);

ALTER TABLE `nextjs_shop_delivery_info` ADD CONSTRAINT `FK_nextjs_shoporder_group_TO_delivery_info_1` FOREIGN KEY (
	`order_id`
)
REFERENCES `nextjs_shop_order_group` (
	`order_id`
);

ALTER TABLE `nextjs_shop_coupon_used` ADD CONSTRAINT `FK_nextjs_shopcoupon_TO_coupon_used_1` FOREIGN KEY (
	`coupon_id`
)
REFERENCES `nextjs_shop_coupon` (
	`coupon_id`
);

ALTER TABLE `nextjs_shop_coupon_used` ADD CONSTRAINT `FK_nextjs_shoporder_group_TO_coupon_used_1` FOREIGN KEY (
	`order_id`
)
REFERENCES `nextjs_shop_order_group` (
	`order_id`
);

ALTER TABLE `nextjs_shop_coupon_allowed` ADD CONSTRAINT `FK_nextjs_shopcoupon_TO_coupon_allowed_1` FOREIGN KEY (
	`coupon_id`
)
REFERENCES `nextjs_shop_coupon` (
	`coupon_id`
);

ALTER TABLE `nextjs_shop_coupon_allowed` ADD CONSTRAINT `FK_nextjs_shopproduct_TO_coupon_allowed_1` FOREIGN KEY (
	`product_id`
)
REFERENCES `nextjs_shop_product` (
	`product_id`
);

ALTER TABLE `nextjs_shop_review` ADD CONSTRAINT `FK_nextjs_shoporder_group_TO_review_1` FOREIGN KEY (
	`order_id`
)
REFERENCES `nextjs_shop_order_group` (
	`order_id`
);

ALTER TABLE `nextjs_shop_token` ADD CONSTRAINT `FK_nextjs_shopuser_TO_token_1` FOREIGN KEY (
	`user_id`
)
REFERENCES `nextjs_shop_user` (
	`user_id`
);