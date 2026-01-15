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