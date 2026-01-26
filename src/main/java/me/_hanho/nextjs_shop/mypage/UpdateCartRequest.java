package me._hanho.nextjs_shop.mypage;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // getter/setter, toString, equals, hashCode 자동 생성
@NoArgsConstructor // 기본 생성자
@AllArgsConstructor // 전체 필드 생성자
public class UpdateCartRequest {
	@NotNull private Integer cartId;
	@NotNull private Integer productOptionId;
	@NotNull private Integer quantity;
}
