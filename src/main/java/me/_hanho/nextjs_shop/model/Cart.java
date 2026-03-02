package me._hanho.nextjs_shop.model;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // getter/setter, toString, equals, hashCode 자동 생성
@NoArgsConstructor // 기본 생성자
@AllArgsConstructor // 전체 필드 생성자
public class Cart {
	private int cartId;
	private Timestamp createdAt;
	private Timestamp updatedAt;
	private int productOptionId;
	private int userNo;
	private int quantity;
	private boolean selected;
}
