package me._hanho.nextjs_shop.common.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

	/* 공통(인증, DB) */
	DB_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "DB_ERROR", "Database error"),
	UNAUTHORIZED_TOKEN(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED_TOKEN", "토큰이 유효하지 않습니다"),
	WRONG_TOKEN(HttpStatus.UNAUTHORIZED, "WRONG_TOKEN", "WRONG_TOKEN"),
	TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "TOKEN_NOT_FOUND", "Token not found"),

	/* 관리자 */

	/* 유저 */
	USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER_NOT_FOUND", "User not found"),

	/* 구매 */

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
	NO_PERMISSION_OR_PRODUCT_NOT_FOUND(HttpStatus.FORBIDDEN, "NO_PERMISSION_OR_PRODUCT_NOT_FOUND", "권한이 없거나 상품이 없습니다");



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
