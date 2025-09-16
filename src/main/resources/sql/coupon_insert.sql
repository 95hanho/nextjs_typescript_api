/* ===============================
   1) 쿠폰 예시 데이터 INSERT
   =============================== */
INSERT INTO nextjs_shop_coupon
(description, coupon_code, discount_type, discount_value, max_discount, minimum_order_before_amount, status, is_stackable, start_date, end_date, seller_id)
VALUES
-- 플랫폼 공용
('플랫폼 10% 할인 (최대 2만원)', 'PLATFORM10P', 'percentage', 10.000000, 20000.000000, 50000.000000, 'Y', 0, '2025-08-01 00:00:00', '2025-12-31 23:59:59', NULL),
('플랫폼 3천원 즉시할인 (스택가능)', 'PLATFORM-3K', 'fixed_amount', 3000.000000, NULL, 30000.000000, 'Y', 1, '2025-08-01 00:00:00', '2025-12-31 23:59:59', NULL),

-- seller01: Urban Style
('Urban Style 15% (최대 3만원)', 'URBAN15P', 'percentage', 15.000000, 30000.000000, 70000.000000, 'Y', 0, '2025-08-01 00:00:00', '2025-12-31 23:59:59', 'seller01'),
('Urban Style 5천원 (스택가능)', 'URBAN-5K', 'fixed_amount', 5000.000000, NULL, 60000.000000, 'Y', 1, '2025-08-01 00:00:00', '2025-12-31 23:59:59', 'seller01'),

-- seller02: Daily Wear
('Daily Wear 1만원', 'DAILY-10K', 'fixed_amount', 10000.000000, NULL, 80000.000000, 'Y', 0, '2025-08-01 00:00:00', '2025-12-31 23:59:59', 'seller02'),

-- seller03: Trend Factory
('Trend Factory 20% (최대 4만원)', 'TREND20P', 'percentage', 20.000000, 40000.000000, 100000.000000, 'Y', 0, '2025-08-01 00:00:00', '2025-12-31 23:59:59', 'seller03'),

-- seller04: Blue Ocean
('Blue Ocean 7천원 (스택가능)', 'BLUE-7K', 'fixed_amount', 7000.000000, NULL, 50000.000000, 'Y', 1, '2025-08-01 00:00:00', '2025-12-31 23:59:59', 'seller04'),

-- seller06: Sporty Fit
('Sporty Fit 10% (최대 2.5만원)', 'SPORTY10P', 'percentage', 10.000000, 25000.000000, 70000.000000, 'Y', 0, '2025-08-01 00:00:00', '2025-12-31 23:59:59', 'seller06'),

-- seller07: Casual Mood
('Casual Mood 5천원 (스택가능)', 'CASUAL-5K', 'fixed_amount', 5000.000000, NULL, 50000.000000, 'Y', 1, '2025-08-01 00:00:00', '2025-12-31 23:59:59', 'seller07'),

-- seller09: Luxury Point
('Luxury Point 2만원', 'LUXE-20K', 'fixed_amount', 20000.000000, NULL, 150000.000000, 'Y', 0, '2025-08-01 00:00:00', '2025-12-31 23:59:59', 'seller09'),

-- seller12: Summer Breeze
('Summer Breeze 12% (최대 3만원)', 'SUMMER-12P', 'percentage', 12.000000, 30000.000000, 60000.000000, 'Y', 0, '2025-08-01 00:00:00', '2025-12-31 23:59:59', 'seller12'),

-- seller13: Winter Warm (비활성 예시)
('Winter Warm 15% (최대 3.5만원)', 'WINTER-15P', 'percentage', 15.000000, 35000.000000, 80000.000000, 'N', 0, '2025-08-01 00:00:00', '2025-12-31 23:59:59', 'seller13');
