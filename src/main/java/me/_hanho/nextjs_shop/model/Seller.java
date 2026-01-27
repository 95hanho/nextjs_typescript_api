package me._hanho.nextjs_shop.model;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Seller {
	private int sellerNo;
    private String sellerId; // 아이디
    private String password; // 비밀번호
    private String sellerName; // 판매자 이름(한글)
    private String sellerNameEn; // 판매자 이름(영어)
    private String extensionNumber; // 내선전화
    private String mobileNumber; // 대표 번호
    private String email; // 이메일
    private String businessRegistrationNumber; // 사업자 등록번호
    private String telecomSalesNumber; // 통신 판매자 번호
    private String representativeName; // 대표자 이름
    private String businessZipcode; // 사업장 소재지 우편번호
    private String businessAddress; // 사업장 소재지 주소
    private String businessAddressDetail; // 사업장 소재지 상세주소 
    private Timestamp updatedAt; // 업데이트 시
    private Timestamp requestedAt; // 판매자 가입 신청 시각
    private String appovalStatus; // 승인 상태 'PENDING','APPROVED','REJECTED','SUSPENDED'
    private Timestamp approvedAt; // 요청/승인 날짜
    private int approvedBy; // 처리자 관리자no
    private Timestamp rejectedAt; // 반려 시각
    private String rejectReason; // 반려 사유
    private Timestamp suspendedAt; // 정지 날짜
}
