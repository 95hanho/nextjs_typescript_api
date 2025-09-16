-- 기존 데이터 초기화
DELETE FROM nextjs_shop_product WHERE seller_id IS NOT NULL;
ALTER TABLE nextjs_shop_product AUTO_INCREMENT = 1;

-- 다시 INSERT (color_name, seller_id, menu_sub_id 채움)
INSERT INTO `nextjs_shop_product`
(product_id, name, color_name, price, created_at, view_count, wish_count, seller_id, menu_sub_id)
VALUES
(1, '투데이 데일리 자켓', 'Black', 150000, '2025-04-07 15:50:03', 0, 0, 'seller01', 53), -- 재킷
(2, 'zi존간지 코트', 'Gray', 15000, '2025-04-07 15:52:31', 0, 0, 'seller02', 2), -- 트렌치/맥코트
(3, '황금 후드티', 'Yellow', 3000000, '2025-04-07 15:54:23', 0, 0, 'seller03', 31), -- 후디
(4, '물방울 무늬 셔츠', 'Blue', 85000, '2025-04-07 16:39:34', 0, 0, 'seller04', 83), -- 셔츠
(5, '벚꽃 자켓', 'Pink', 230000, '2025-04-07 16:39:42', 0, 0, 'seller05', 53), -- 재킷
(6, '땡글미러 썬글라스', 'Silver', 100000, '2025-04-07 16:39:51', 0, 0, 'seller06', 63), -- 기타 아우터 (임시)
(7, '아저씨핏 셔츠', 'White', 120000, '2025-04-07 16:39:57', 0, 0, 'seller07', 83), -- 셔츠
(8, '자두 원피스', 'Red', 80000, '2025-04-07 16:40:03', 0, 0, 'seller08', 107), -- 롱 원피스
(9, '더블 쓰리핏 티셔츠', 'Navy', 53000, '2025-04-07 16:40:10', 0, 0, 'seller09', 81), -- 반소매 티셔츠
(10, '가운같은가운아닌원피스', 'Beige', 4500000, '2025-04-07 16:40:13', 0, 0, 'seller10', 110); -- 데님 원피스

INSERT INTO nextjs_shop_product
(product_id, name, color_name, price, created_at, view_count, wish_count, seller_id, menu_sub_id)
VALUES
(11, '슬림핏 슬렉스', 'Black', 69000, NOW() - INTERVAL 1 DAY, 120, 15, 'seller01', 42),
(12, '스트레이트 데님 팬츠', 'Blue', 59000, NOW() - INTERVAL 2 DAY, 85, 10, 'seller02', 40),
(13, '와이드 치노 팬츠', 'Beige', 64000, NOW() - INTERVAL 3 DAY, 142, 19, 'seller03', 43),
(14, '트레이닝 조거 팬츠', 'Gray', 49000, NOW() - INTERVAL 4 DAY, 210, 25, 'seller04', 41),
(15, '부츠컷 팬츠', 'Navy', 72000, NOW() - INTERVAL 5 DAY, 65, 8, 'seller05', 36),
(16, '레깅스 퍼포먼스핏', 'Black', 39000, NOW() - INTERVAL 6 DAY, 102, 12, 'seller06', 34),
(17, '하프코트 모던핏', 'Gray', 159000, NOW() - INTERVAL 7 DAY, 178, 22, 'seller07', 61),
(18, '롱코트 클래식', 'Camel', 189000, NOW() - INTERVAL 8 DAY, 200, 26, 'seller08', 20),
(19, '경량 패딩 자켓', 'Khaki', 89000, NOW() - INTERVAL 9 DAY, 250, 33, 'seller09', 21),
(20, '숏패딩 액티브핏', 'White', 129000, NOW() - INTERVAL 10 DAY, 300, 40, 'seller10', 73),

(21, '롱패딩 프리미엄', 'Black', 219000, NOW() - INTERVAL 11 DAY, 410, 55, 'seller11', 79),
(22, '바람막이 윈드브레이커', 'Navy', 69000, NOW() - INTERVAL 12 DAY, 170, 20, 'seller12', 11),
(23, '점퍼 베이직핏', 'Green', 75000, NOW() - INTERVAL 13 DAY, 155, 19, 'seller13', 55),
(24, '트렌치코트 더블', 'Beige', 169000, NOW() - INTERVAL 14 DAY, 230, 28, 'seller14', 2),
(25, '데님 자켓 오버핏', 'Blue', 85000, NOW() - INTERVAL 15 DAY, 188, 23, 'seller15', 7),
(26, '블루종 크롭핏', 'Black', 109000, NOW() - INTERVAL 16 DAY, 115, 14, 'seller01', 5),
(27, '바시티 자켓', 'Wine', 129000, NOW() - INTERVAL 17 DAY, 180, 21, 'seller02', 6),
(28, '플리스 풀집업', 'Ivory', 59000, NOW() - INTERVAL 18 DAY, 240, 30, 'seller03', 1),
(29, '후드 집업 코지핏', 'Gray', 69000, NOW() - INTERVAL 19 DAY, 220, 28, 'seller04', 14),
(30, '아노락 윈드자켓', 'Navy', 99000, NOW() - INTERVAL 20 DAY, 140, 17, 'seller05', 12),

(31, '긴소매 티셔츠 베이직', 'White', 29000, NOW() - INTERVAL 21 DAY, 350, 42, 'seller06', 32),
(32, '반소매 티셔츠 에센셜', 'Black', 19000, NOW() - INTERVAL 22 DAY, 420, 51, 'seller07', 25),
(33, '피케 카라 티셔츠', 'Navy', 39000, NOW() - INTERVAL 23 DAY, 275, 33, 'seller08', 28),
(34, '긴소매 셔츠 옥스포드', 'Blue', 59000, NOW() - INTERVAL 24 DAY, 198, 22, 'seller09', 33),
(35, '후디 오버핏', 'Gray', 49000, NOW() - INTERVAL 25 DAY, 300, 35, 'seller10', 31),
(36, '스웨트셔츠 루즈핏', 'Beige', 45000, NOW() - INTERVAL 26 DAY, 280, 32, 'seller11', 29),
(37, '슬리브리스 크롭', 'Black', 25000, NOW() - INTERVAL 27 DAY, 120, 18, 'seller12', 27),
(38, '블라우스 셔링', 'Ivory', 65000, NOW() - INTERVAL 28 DAY, 155, 20, 'seller13', 80),
(39, '폴로 셔츠 베이직', 'Navy', 39000, NOW() - INTERVAL 29 DAY, 165, 21, 'seller14', 50),
(40, '터틀넥 니트', 'Brown', 59000, NOW() - INTERVAL 30 DAY, 180, 24, 'seller15', 48),

(41, '카디건 루즈핏', 'Gray', 69000, NOW() - INTERVAL 31 DAY, 142, 18, 'seller01', 49),
(42, '브이넥 니트', 'Ivory', 59000, NOW() - INTERVAL 32 DAY, 200, 26, 'seller02', 46),
(43, '크루넥 니트', 'Beige', 49000, NOW() - INTERVAL 33 DAY, 210, 27, 'seller03', 45),
(44, '베스트 니트', 'Black', 42000, NOW() - INTERVAL 34 DAY, 110, 13, 'seller04', 51),
(45, '점프수트 유틸리티', 'Khaki', 89000, NOW() - INTERVAL 35 DAY, 130, 16, 'seller05', 111),
(46, '롱 원피스 플레어', 'Red', 129000, NOW() - INTERVAL 36 DAY, 300, 42, 'seller06', 107),
(47, '미니 원피스', 'Pink', 89000, NOW() - INTERVAL 37 DAY, 210, 28, 'seller07', 108),
(48, '미디 원피스', 'Navy', 99000, NOW() - INTERVAL 38 DAY, 185, 25, 'seller08', 109),
(49, '데님 원피스', 'Blue', 109000, NOW() - INTERVAL 39 DAY, 155, 19, 'seller09', 110),
(50, '캐시미어 니트', 'Beige', 159000, NOW() - INTERVAL 40 DAY, 260, 34, 'seller10', 120);


