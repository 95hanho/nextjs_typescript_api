package me._hanho.nextjs_shop.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // getter/setter, toString, equals, hashCode 자동 생성
@NoArgsConstructor // 기본 생성자
@AllArgsConstructor // 전체 필드 생성자
public class AdminLoginDTO {
	private int adminNo;
	private String adminId;
	private String password;
}
