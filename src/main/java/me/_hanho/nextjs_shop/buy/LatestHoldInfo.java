package me._hanho.nextjs_shop.buy;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LatestHoldInfo {
    
	private int holdId;
	private String returnUrl;
	private String status; // 'HOLD','PAY','RELEASED'
	private boolean activeHold;
	private Timestamp expiresAt;

	private int stock;
    private boolean isDisplayed;

    private int productId;
    private boolean saleStop;

    private String approvalStatus;
}
