UPDATE nextjs_shop_product
SET
  material_info            = COALESCE(material_info, '겉감: 면 100%'),
  color_info               = COALESCE(color_info, CONCAT('상세페이지 참조 (대표색상: ', color_name, ')')),
  size_info                = COALESCE(size_info, '상세페이지 참조'),
  manufacturer_name        = COALESCE(manufacturer_name, '㈜이터널그룹'),
  country_of_origin        = COALESCE(country_of_origin, '대한민국'),
  wash_care_info           = COALESCE(wash_care_info, '상세페이지 참조 (케어라벨 참조)'),
  manufactured_ym          = COALESCE(manufactured_ym, DATE_FORMAT(created_at, '%Y%m')),
  quality_guarantee_info   = COALESCE(quality_guarantee_info, '관련법 및 소비자분쟁해결 규정에 따름'),
  after_service_contact    = COALESCE(after_service_contact, '(주)이터널그룹/080-202-2023'),
  after_service_manager    = COALESCE(after_service_manager, '(주)이터널그룹'),
  after_service_phone      = COALESCE(after_service_phone, '080-202-2023')
WHERE seller_id = 'seller01';

UPDATE nextjs_shop_seller
SET approval_status = 'APPROVED';

UPDATE nextjs_shop_user
SET withdrawal_status = 'ACTIVE';

UPDATE nextjs_shop_coupon
SET `status` = 'ACTIVE';

UPDATE nextjs_shop_brand_bookmark bb
JOIN nextjs_shop_seller s
  ON bb.seller_id = s.seller_id
SET bb.seller_no = s.seller_no;

UPDATE nextjs_shop_brand_bookmark bb
JOIN nextjs_shop_user s
  ON bb.user_id = s.user_id
SET bb.user_no = s.user_no;

UPDATE nextjs_shop_coupon c
JOIN nextjs_shop_seller s
  ON c.seller_id = s.seller_id
SET c.seller_no = s.seller_no;


UPDATE nextjs_shop_phone_auth pa
JOIN nextjs_shop_user u
  ON pa.user_id = u.user_id
SET pa.user_no = u.user_no;

UPDATE nextjs_shop_product c
JOIN nextjs_shop_seller s
  ON c.seller_id = s.seller_id
SET c.seller_no = s.seller_no;

UPDATE nextjs_shop_product_qna pa
JOIN nextjs_shop_user u
  ON pa.user_id = u.user_id
SET pa.user_no = u.user_no;

UPDATE nextjs_shop_product_view pa
JOIN nextjs_shop_user u
  ON pa.user_id = u.user_id
SET pa.user_no = u.user_no;

UPDATE nextjs_shop_review pa
JOIN nextjs_shop_user u
  ON pa.user_id = u.user_id
SET pa.user_no = u.user_no;

UPDATE nextjs_shop_stock_hold pa
JOIN nextjs_shop_user u
  ON pa.user_id = u.user_id
SET pa.user_no = u.user_no;

UPDATE nextjs_shop_user_address pa
JOIN nextjs_shop_user u
  ON pa.user_id = u.user_id
SET pa.user_no = u.user_no;

UPDATE nextjs_shop_user_coupon pa
JOIN nextjs_shop_user u
  ON pa.user_id = u.user_id
SET pa.user_no = u.user_no;

UPDATE nextjs_shop_wish pa
JOIN nextjs_shop_user u
  ON pa.user_id = u.user_id
SET pa.user_no = u.user_no;

UPDATE nextjs_shop_cart pa
JOIN nextjs_shop_user u
  ON pa.user_id = u.user_id
SET pa.user_no = u.user_no;

UPDATE nextjs_shop_order_group pa
JOIN nextjs_shop_user u
  ON pa.user_id = u.user_id
SET pa.user_no = u.user_no;

SELECT refresh_token, COUNT(*) cnt
FROM nextjs_shop_token
WHERE refresh_token IS NOT NULL
GROUP BY refresh_token
HAVING cnt > 1;

CREATE INDEX idx_token_lookup
ON nextjs_shop_token (refresh_token, connect_ip, connect_agent(50), created_at, token_id);
CREATE UNIQUE INDEX uq_refresh_token
ON nextjs_shop_token (refresh_token);

CREATE UNIQUE INDEX uq_refresh_token
ON nextjs_shop_token (refresh_token);

DROP INDEX idx_token_lookup ON nextjs_shop_token;


SELECT EXISTS (
     SELECT 1 FROM nextjs_shop_seller
     WHERE seller_id = 'expert'
 ) AS exists_flag;
 
 UPDATE nextjs_shop_product
SET final_price = FLOOR((origin_price * (0.60 + (RAND() * 0.35))) / 100) * 100
WHERE final_price IS NULL
  AND origin_price IS NOT NULL
  AND origin_price > 0;
  
  UPDATE nextjs_shop_product
SET origin_price = ROUND(origin_price / 100) * 100
WHERE origin_price IS NOT NULL
  AND origin_price > 0;
  
UPDATE nextjs_shop_product_option
SET is_displayed = 1;


