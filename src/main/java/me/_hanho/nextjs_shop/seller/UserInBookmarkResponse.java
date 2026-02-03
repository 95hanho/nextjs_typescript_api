package me._hanho.nextjs_shop.seller;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInBookmarkResponse {
	
	private Timestamp createdAt;
	private String userId;
	private String userName;
}
