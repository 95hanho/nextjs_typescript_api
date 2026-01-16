package me._hanho.nextjs_shop.admin;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SellerApprovalRequest {
	private List<Integer> sellerNoList;
	private String approvalStatus; // PENDING / APPROVED / REJECTED / SUSPENDED
	private int approvedBy; // APPROVED 시 필수
	private String rejectReason;   // REJECTED 시 필수
}
