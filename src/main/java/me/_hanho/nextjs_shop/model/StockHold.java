package me._hanho.nextjs_shop.model;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockHold {
	private int holdId;
	private String userId;
	private int productDetailId;
	private int count;
	private String status;
	private boolean activeHold;
	private Timestamp expiresAt;
	private Timestamp createdAt;
}
