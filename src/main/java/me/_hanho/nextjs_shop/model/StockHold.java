package me._hanho.nextjs_shop.model;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockHold {
	private int hold_id;
	private String user_id;
	private int product_detail_id;
	private int count;
	private String status;
	private boolean active_hold;
	private Timestamp expires_at;
	private Timestamp created_at;
}
