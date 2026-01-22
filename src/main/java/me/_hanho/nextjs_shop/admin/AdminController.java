package me._hanho.nextjs_shop.admin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me._hanho.nextjs_shop.auth.AuthService;
import me._hanho.nextjs_shop.auth.TokenDTO;
import me._hanho.nextjs_shop.auth.TokenService;
import me._hanho.nextjs_shop.common.exception.BusinessException;
import me._hanho.nextjs_shop.common.exception.ErrorCode;
import me._hanho.nextjs_shop.model.Seller;
import me._hanho.nextjs_shop.seller.SellerRegisterRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bapi/admin")
public class AdminController {

	private static final Logger logger = LoggerFactory.getLogger(AdminController.class);
	
	private final AdminService adminService;
	private final TokenService tokenService;
	private final AuthService authService;
	
	// 암호화 비번 출력
	@PostMapping("/hash-password")
	public ResponseEntity<Map<String, Object>> getEncryptionPassword(@RequestParam("password") String password) {
		logger.info("getEncryptionPassword :" + password);
		Map<String, Object> result = new HashMap<String, Object>();
		
		String encryptionPassword = adminService.getEncryptionPassword(password);
		
		result.put("password", password);
		result.put("encryptionPassword", encryptionPassword);
		result.put("message", "SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	// 관리자 정보가져오기
	@GetMapping
	public ResponseEntity<Map<String, Object>> getAdminInfo(@RequestAttribute(name = "adminNo", required = false) Integer adminNo) {
		if (adminNo == null) throw new BusinessException(ErrorCode.AUTHENTICATION_REQUIRED);
		logger.info("getAdminNoInfo : adminNo=" + adminNo);
		Map<String, Object> result = new HashMap<String, Object>();
		
		AdminInfo admin = adminService.getAdminInfo(adminNo);
		
		result.put("admin", admin);
		result.put("message", "ADMIN_FETCH_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	// 로그인
	@PostMapping
	public ResponseEntity<Map<String, Object>> login(@RequestParam("adminId") String adminId, @RequestParam("password") String password) {
		logger.info("adminLogin :" + adminId);
		Map<String, Object> result = new HashMap<String, Object>();
		
		AdminLoginDTO checkAdmin = adminService.isAdmin(adminId);
		if (checkAdmin == null || !adminService.passwordCheck(password, checkAdmin.getPassword())) {
			result.put("message", "SELLER_NOT_FOUND"); // 입력하신 아이디 또는 비밀번호가 일치하지 않습니다
			logger.error("입력하신 아이디 또는 비밀번호가 일치하지 않습니다");
			
			return new ResponseEntity<>(
					result
					, HttpStatus.UNAUTHORIZED);
		} else {
			// 마지막 로그인 표시
			adminService.updateLastLoginAt(checkAdmin.getAdminNo());
			
			result.put("adminNo", checkAdmin.getAdminNo());
			result.put("message", "LOGIN_SUCCESS");
			return new ResponseEntity<>(
					result
					, HttpStatus.OK);
		}
	}
	// 로그인 토큰 저장
	@PostMapping("/token")
	public ResponseEntity<Map<String, Object>> tokenStore(
			@RequestAttribute(name = "adminNo", required = false) Integer adminNo, 
			@RequestParam("refreshToken") String refreshToken,
			@RequestHeader("user-agent") String userAgent, 
			@RequestHeader("x-forwarded-for") String forwardedFor) {
		if (adminNo == null) throw new BusinessException(ErrorCode.AUTHENTICATION_REQUIRED);
		logger.info("insertToken refreshToken : " + refreshToken.substring(refreshToken.length() - 10) + ", adminNo : " + adminNo + 
				", user-agent : " + userAgent + ", x-forwarded-for : " + forwardedFor);
		Map<String, Object> result = new HashMap<String, Object>();
		
		tokenService.parseJwtRefreshToken(refreshToken);
		
		String ipAddress = forwardedFor != null ? forwardedFor : "unknown";
		AdminToken token = AdminToken.builder().connectIp(ipAddress).connectAgent(userAgent).refreshToken(refreshToken).adminNo(adminNo).build(); 
		
		adminService.insertToken(token);
		
		result.put("message", "ADMIN_TOKEN_INSERT_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	// 로그인 토큰 수정(재저장)
	@PostMapping("/token/refresh")
	public ResponseEntity<Map<String, Object>> updateToken(
			@RequestParam("beforeToken") String beforeToken , @RequestParam("refreshToken") String refreshToken,
			@RequestHeader("user-agent") String userAgent, @RequestHeader("x-forwarded-for") String forwardedFor) {
		logger.info("updateToken beforeToken : " + beforeToken.substring(beforeToken.length() - 10) + 
				", refreshToken : " + refreshToken.substring(refreshToken.length() - 10) + ", user-agent : " + userAgent + 
				", x-forwarded-for : " + forwardedFor);
		Map<String, Object> result = new HashMap<String, Object>();
		
		tokenService.parseJwtRefreshToken(beforeToken);
		tokenService.parseJwtRefreshToken(refreshToken);
		
		String ipAddress = forwardedFor != null ? forwardedFor : "unknown";
		TokenDTO token = TokenDTO.builder().connectIp(ipAddress).connectAgent(userAgent).refreshToken(refreshToken).beforeToken(beforeToken).build(); 
		
		authService.updateToken(token);
		
		Integer adminNo = adminService.getAdminNoByToken(token);
		if (adminNo == null) {
	        throw new BusinessException(ErrorCode.WRONG_TOKEN);
	    }
		
		result.put("adminNo", adminNo);
		result.put("message", "TOKEN_UPDATE_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	// 판매자 조회
	@GetMapping("/seller")
	public ResponseEntity<Map<String, Object>> getSellerList(@RequestAttribute(name = "adminNo", required = false) Integer adminNo) {
		if (adminNo == null) throw new BusinessException(ErrorCode.AUTHENTICATION_REQUIRED);
		logger.info("getSellerList " + adminNo);
		Map<String, Object> result = new HashMap<String, Object>();

		List<Seller> sellerList = adminService.getSellerList();
		
		result.put("sellerList", sellerList);
		result.put("message", "SELLER_FETCH_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	// 판매자 추가
	@PostMapping("/seller")
	public ResponseEntity<Map<String, Object>> addSeller(@Valid @ModelAttribute SellerRegisterRequest seller,
			@RequestAttribute(name = "adminNo", required = false) Integer adminNo) {
		if (adminNo == null) throw new BusinessException(ErrorCode.AUTHENTICATION_REQUIRED);
		logger.info("addSeller " + seller);
		Map<String, Object> result = new HashMap<String, Object>();

		if(adminService.hasSeller(seller.getSellerId())) {
			throw new BusinessException(ErrorCode.SELLER_DUPLICATED);
		}
		
		adminService.addSeller(seller, adminNo);
		
		result.put("message", "SELLER_ADD_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	// 판매자 승인여부 변경
	@PostMapping("/seller/approval")
	public ResponseEntity<Map<String, Object>> setSellerApproval(@Valid @ModelAttribute SellerApprovalRequest sellerApproval,
			@RequestAttribute(name = "adminNo", required = false) Integer adminNo) {
		if (adminNo == null) throw new BusinessException(ErrorCode.AUTHENTICATION_REQUIRED);
		logger.info("setSellerApproval {} : " + sellerApproval);
		Map<String, Object> result = new HashMap<String, Object>();
		
		if("REJECTED".equals(sellerApproval.getApprovalStatus()) && sellerApproval.getRejectReason() == null) {
			throw new BusinessException(ErrorCode.AUTHENTICATION_REQUIRED);
		}
		
		adminService.setSellerApproval(sellerApproval, adminNo);
		
		result.put("message", "SELLER_APPROVE_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	// 회원 조회
	@GetMapping("/user")
	public ResponseEntity<Map<String, Object>> getUserList(
			@RequestAttribute(name = "adminNo", required = false) Integer adminNo) {
		if (adminNo == null) throw new BusinessException(ErrorCode.AUTHENTICATION_REQUIRED);
		logger.info("getUserList adminNo : " + adminNo);
		Map<String, Object> result = new HashMap<String, Object>();
		
		List<UserResponse> userList = adminService.getUserList();
		
		result.put("userList", userList);
		result.put("message", "USER_LIST_FETCH_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	// 회원 정보 보기(마스킹해제)
	@PostMapping("/user")
	public ResponseEntity<Map<String, Object>> getUserInfoUnmasked(
			@RequestParam("userNo") Integer userNo,
			@RequestAttribute(name = "adminNo", required = false) Integer adminNo) {
		if (adminNo == null) throw new BusinessException(ErrorCode.AUTHENTICATION_REQUIRED);
		logger.info("getUserInfoUnmasked userNo : " + userNo);
		Map<String, Object> result = new HashMap<String, Object>();
		
		UserInfoResponse userInfo = adminService.getUserInfoUnmasked(userNo);
		
		result.put("userInfo", userInfo);
		result.put("message", "USER_UNMASKED_FETCH_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	// 회원 탈퇴 확정하기 -> 이거 한 후 1년 뒤 진짜 정보 DELETE이런거 해도 좋을듯??
	@PostMapping("/user/withdrawal")
	public ResponseEntity<Map<String, Object>> updateUserWithdrawalStatus(
			@RequestParam("userNoList") List<Integer> userNoList,
			@RequestParam("withdrawalStatus") String withdrawalStatus,
			@RequestAttribute(name = "adminNo", required = false) Integer adminNo) {
		if (adminNo == null) throw new BusinessException(ErrorCode.AUTHENTICATION_REQUIRED);
		logger.info("updateUserWithdrawalStatus userNoList {} : " + userNoList + "withdrawalStatus : " + withdrawalStatus);
		Map<String, Object> result = new HashMap<String, Object>();
		
		adminService.updateUserWithdrawalStatus(userNoList, withdrawalStatus);
		
		result.put("message", "USER_WITHDRAWAL_STATUS_UPDATE_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	// 공용 쿠폰 조회
	@GetMapping("/coupon/common")
	public ResponseEntity<Map<String, Object>> getCommonCouponList(
			@RequestAttribute(name = "adminNo", required = false) Integer adminNo) {
		if (adminNo == null) throw new BusinessException(ErrorCode.AUTHENTICATION_REQUIRED);
		logger.info("getCommonCouponList  : ");
		Map<String, Object> result = new HashMap<String, Object>();
		
		List<CommonCoupon> commonCouponList = adminService.getCommonCouponList();
		
		result.put("commonCouponList", commonCouponList);
		result.put("message", "COMMON_COUPON_FETCH_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	// 공용 쿠폰 등록
	@PostMapping("/coupon/common")
	public ResponseEntity<Map<String, Object>> addCommonCoupon(
			@Valid @ModelAttribute AddCommonCouponRequest commonCoupon,
			@RequestAttribute(name = "adminNo", required = false) Integer adminNo) {
		if (adminNo == null) throw new BusinessException(ErrorCode.AUTHENTICATION_REQUIRED);
		logger.info("addCommonCoupon commonCoupon {} : " + commonCoupon);
		Map<String, Object> result = new HashMap<String, Object>();
		
		// discount_type에 따른 규칙
		if ("percentage".equals(commonCoupon.getDiscountType())) {
		    if (commonCoupon.getMaxDiscount() == null) {
		        throw new BusinessException(ErrorCode.COUPON_MAX_DISCOUNT_REQUIRED_FOR_PERCENTAGE);
		    }
		} else if ("fixed_amount".equals(commonCoupon.getDiscountType())) {
		    if (commonCoupon.getMaxDiscount() != null) {
		        throw new BusinessException(ErrorCode.COUPON_MAX_DISCOUNT_MUST_BE_NULL_FOR_FIXED_AMOUNT);
		    }
		} else {
		    throw new BusinessException(ErrorCode.COUPON_INVALID_DISCOUNT_TYPE);
		}
		
		adminService.addCommonCoupon(commonCoupon, adminNo);
		
		result.put("message", "COMMON_COUPON_ADD_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	// 공용 쿠폰 수정 -> 나중에 admin권한 추가해도 될듯
	@PutMapping("/coupon/common")
	public ResponseEntity<Map<String, Object>> updateCommonCoupon(
			@Valid @ModelAttribute UpdateCommonCouponRequest commonCoupon,
			@RequestAttribute(name = "adminNo", required = false) Integer adminNo) {
		if (adminNo == null) throw new BusinessException(ErrorCode.AUTHENTICATION_REQUIRED);
		logger.info("updateCommonCoupon commonCoupon {} : " + commonCoupon);
		Map<String, Object> result = new HashMap<String, Object>();
		
		// discount_type에 따른 규칙
		if ("percentage".equals(commonCoupon.getDiscountType())) {
		    if (commonCoupon.getMaxDiscount() == null) {
		        throw new BusinessException(ErrorCode.COUPON_MAX_DISCOUNT_REQUIRED_FOR_PERCENTAGE);
		    }
		} else if ("fixed_amount".equals(commonCoupon.getDiscountType())) {
		    if (commonCoupon.getMaxDiscount() != null) {
		        throw new BusinessException(ErrorCode.COUPON_MAX_DISCOUNT_MUST_BE_NULL_FOR_FIXED_AMOUNT);
		    }
		} else {
		    throw new BusinessException(ErrorCode.COUPON_INVALID_DISCOUNT_TYPE);
		}
		
		adminService.updateCommonCoupon(commonCoupon);
		
		result.put("message", "COMMON_COUPON_UPDATE_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	// 공용 쿠폰 삭제 -> 나중에 admin권한 추가해도 될듯
	@DeleteMapping("/coupon/common/{couponId}")
	public ResponseEntity<Map<String, Object>> deleteCommonCoupon(
			@PathVariable("couponId") Integer couponId,
			@RequestAttribute(name = "adminNo", required = false) Integer adminNo) {
		if (adminNo == null) throw new BusinessException(ErrorCode.AUTHENTICATION_REQUIRED);
		logger.info("deleteCommonCoupon couponId : " + couponId);
		Map<String, Object> result = new HashMap<String, Object>();
		
		adminService.deleteCommonCoupon(couponId);
		
		result.put("message", "COMMON_COUPON_DELETE_SUCCESS");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
}
