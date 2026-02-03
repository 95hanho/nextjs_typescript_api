package me._hanho.nextjs_shop.seller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SellerLogin {
	private int sellerNo;
    private String sellerId; // 아이디
    private String password; // 비밀번호
}
