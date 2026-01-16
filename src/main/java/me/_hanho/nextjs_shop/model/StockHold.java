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
	private int userNo;
	private int productOptionId;
	private int count;
	private String status; // 'HOLD','PAY','RELEASED'
	private boolean activeHold;
	private Timestamp expiresAt;
	private Timestamp createdAt;
}
