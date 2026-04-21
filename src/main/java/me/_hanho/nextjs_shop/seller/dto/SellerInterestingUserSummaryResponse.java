package me._hanho.nextjs_shop.seller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SellerInterestingUserSummaryResponse {
    private int viewedUserCount;       // 내 상품 본 회원 수
    private int wishedUserCount;       // 위시한 회원 수
    private int bookmarkedUserCount;   // 브랜드 즐겨찾기한 회원 수
    private int cartUserCount;         // 장바구니 담은 회원 수
    private int orderedUserCount;      // 구매한 회원 수 (나중용)
}
