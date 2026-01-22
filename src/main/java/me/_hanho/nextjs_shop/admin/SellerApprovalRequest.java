package me._hanho.nextjs_shop.admin;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SellerApprovalRequest {
	@NotNull private List<Integer> sellerNoList;
	@NotBlank private String approvalStatus; // PENDING / APPROVED / REJECTED / SUSPENDED
	private String rejectReason;   // REJECTED 시 필수
}
