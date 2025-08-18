package me._hanho.nextjs_shop.common.exception;

public class CustomDatabaseException extends BusinessException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public CustomDatabaseException(Throwable cause) {
        super(ErrorCode.DB_ERROR, "Database error", cause);
    }
    public CustomDatabaseException(String message, Throwable cause) {
        super(ErrorCode.DB_ERROR, message, cause);
    }
}
