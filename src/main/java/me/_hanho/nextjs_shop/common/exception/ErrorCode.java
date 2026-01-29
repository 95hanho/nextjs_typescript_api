package me._hanho.nextjs_shop.common.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

	/* 공통(인증, DB) */
	DB_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "DB_ERROR", "Database error"),
	UNAUTHORIZED_TOKEN(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED_TOKEN", "토큰이 유효하지 않습니다"),
	AUTHENTICATION_REQUIRED(HttpStatus.UNAUTHORIZED, "AUTHENTICATION_REQUIRED", "인증이 필요합니다"),
	WRONG_TOKEN(HttpStatus.UNAUTHORIZED, "WRONG_TOKEN", "WRONG_TOKEN"),
	TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "TOKEN_NOT_FOUND", "Token not found"),

	/* 공통(request) */
	BAD_REQUEST(HttpStatus.BAD_REQUEST, "BAD_REQUEST", "잘못된 요청입니다"),

	/* 관리자 */
	SELLER_DUPLICATED(HttpStatus.BAD_REQUEST, "SELLER_DUPLICATED", "판매자 중복 (이미 존재) 동일 판매자 존재로 추가 불가"),
	REASON_NOT_FOUND(HttpStatus.BAD_REQUEST, "SELLER_DUPLICATED", "승인거부 시 이유가 함께 필요합니다."),
	
	/* 유저 */
	LOGIN_FAILED(HttpStatus.UNAUTHORIZED, "LOGIN_FAILED", "아이디 또는 비밀번호가 일치하지 않습니다"),
	USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER_NOT_FOUND", "존재하지 않는 사용자입니다"),
	ID_DUPLICATED(HttpStatus.CONFLICT, "ID_DUPLICATED", "중복된 아이디 입니다."),
	//
	PHONE_AUTH_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "PHONE_AUTH_TOKEN_EXPIRED", "휴대폰 인증 토큰이 만료되었습니다"),
	PHONE_AUTH_TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "PHONE_AUTH_TOKEN_INVALID", "휴대폰 인증 토큰이 유효하지 않습니다"),
	PHONE_AUTH_TOKEN_WRONG_TYPE(HttpStatus.UNAUTHORIZED, "PHONE_AUTH_TOKEN_WRONG_TYPE", "휴대폰 인증 토큰 타입이 올바르지 않습니다"),
	PHONE_DUPLICATED(HttpStatus.CONFLICT, "PHONE_DUPLICATED", "이미 존재하는 번호입니다."),
	//
	PHONE_AUTH_NOT_FOUND(HttpStatus.UNAUTHORIZED, "PHONE_AUTH_NOT_FOUND", "휴대폰 인증 정보가 존재하지 않습니다"),
	INVALID_VERIFICATION_CODE(HttpStatus.BAD_REQUEST, "INVALID_VERIFICATION_CODE", "인증번호가 올바르지 않습니다"),
	USER_NO_NOT_FOUND(HttpStatus.NOT_FOUND, "USER_NO_NOT_FOUND", "유저 정보가 없습니다."),
	//
	PWD_RESET_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "PWD_RESET_TOKEN_EXPIRED", "비밀번호 변경 토큰이 만료되었습니다"),
	PWD_RESET_TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "PWD_RESET_TOKEN_INVALID", "비밀번호 변경 토큰이 유효하지 않습니다"),
	PWD_RESET_TOKEN_WRONG_TYPE(HttpStatus.UNAUTHORIZED, "PWD_RESET_TOKEN_WRONG_TYPE", "비밀번호 변경 토큰 타입이 올바르지 않습니다"),
	USER_NO_NOT_FOUND_IN_TOKEN(HttpStatus.BAD_REQUEST, "USER_NO_NOT_FOUND_IN_TOKEN", "토큰에 유저 정보가 없습니다"),
	//
	CURRENT_PASSWORD_MISMATCH(HttpStatus.BAD_REQUEST, "CURRENT_PASSWORD_MISMATCH", "현재 비밀번호가 일치하지않습니다."),
	CURRENT_PASSWORD_EQUAL(HttpStatus.BAD_REQUEST, "CURRENT_PASSWORD_EQUAL", "현재 비밀번호와 새 비밀번호가 일치합니다."),
	

	/* 구매 */
	STOCK_HOLD_FAILED(HttpStatus.CONFLICT, "STOCK_HOLD_FAILED", "상품 확인 및 점유에 실패하였습니다."),
	STOCK_HOLD_UPSERT_INCOMPLETE(HttpStatus.CONFLICT, "STOCK_HOLD_UPSERT_INCOMPLETE", "상품 점유 수정, 삽입 중 오류가 발생했습니다."),
	//
	HOLD_IDS_REQUIRED(HttpStatus.BAD_REQUEST, "HOLD_IDS_REQUIRED", "요청 연장 ID가 없습니다."),
	STOCK_HOLD_EXPIRED(HttpStatus.CONFLICT, "STOCK_HOLD_EXPIRED", "상품들 점유 시간 만료되었습니다."),
	STOCK_HOLD_PARTIAL_EXPIRED(HttpStatus.CONFLICT, "STOCK_HOLD_PARTIAL_EXPIRED", "일부 상품들 점유시간이 만료되었습니다."),

	/* 파일 */

	/* 메인 */

	/* 마이페이지 */
	CART_NOT_FOUND(HttpStatus.NOT_FOUND, "CART_NOT_FOUND", "Cart not found"),
	ADDRESS_NOT_FOUND(HttpStatus.NOT_FOUND, "ADDRESS_NOT_FOUND", "Address not found"),

	/* 제품 */
	PRODUCTDETAIL_NOT_FOUND(HttpStatus.NOT_FOUND, "PRODUCTDETAIL_NOT_FOUND", "ProductOption not found"),
	PRODUCT_OPTION_NOT_FOUND(HttpStatus.NOT_FOUND, "PRODUCT_OPTION_NOT_FOUND", "Product option not found"),
	PRODUCT_OPTION_SIZE_DUPLICATED(HttpStatus.CONFLICT, "PRODUCT_OPTION_SIZE_DUPLICATED", "이미 존재하는 옵션입니다"),

	/* 판매자 */
	NO_PERMISSION_OR_PRODUCT_NOT_FOUND(HttpStatus.FORBIDDEN, "NO_PERMISSION_OR_PRODUCT_NOT_FOUND", "권한이 없거나 상품이 없습니다"),
	// 쿠폰
    COUPON_MAX_DISCOUNT_REQUIRED_FOR_PERCENTAGE(HttpStatus.BAD_REQUEST,"COUPON_MAX_DISCOUNT_REQUIRED_FOR_PERCENTAGE","percentage 쿠폰은 최대 할인 금액(maxDiscount)이 필수입니다"),
    COUPON_MAX_DISCOUNT_MUST_BE_NULL_FOR_FIXED_AMOUNT(HttpStatus.BAD_REQUEST,"COUPON_MAX_DISCOUNT_MUST_BE_NULL_FOR_FIXED_AMOUNT","fixed_amount 쿠폰은 최대 할인 금액을 가질 수 없습니다"),
    COUPON_INVALID_DISCOUNT_TYPE(HttpStatus.BAD_REQUEST,"COUPON_INVALID_DISCOUNT_TYPE","유효하지 않은 할인 타입입니다");

	private final HttpStatus status;
	private final String code;
	private final String defaultMessage;

	ErrorCode(HttpStatus status, String code, String defaultMessage) {
		this.status = status;
		this.code = code;
		this.defaultMessage = defaultMessage;
	}

	public HttpStatus getStatus() { return status; }
	public String getCode() { return code; }
	public String getDefaultMessage() { return defaultMessage; }
}
