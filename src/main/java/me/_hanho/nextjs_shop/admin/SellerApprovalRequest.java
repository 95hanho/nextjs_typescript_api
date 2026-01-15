package me._hanho.nextjs_shop.admin;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SellerApprovalRequest {
	private List<String> sellerIds;
	private String approvalStatus;
}
