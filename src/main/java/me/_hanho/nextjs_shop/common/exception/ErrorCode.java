package me._hanho.nextjs_shop.common.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
	 USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER_NOT_FOUND", "User not found"),
    DB_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "DB_ERROR", "Database error"),
    PRODUCTDETAIL_NOT_FOUND(HttpStatus.NOT_FOUND, "PRODUCTDETAIL_NOT_FOUND", "ProductDetail not found");

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
