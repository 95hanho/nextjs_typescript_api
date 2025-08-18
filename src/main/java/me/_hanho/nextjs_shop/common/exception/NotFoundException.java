package me._hanho.nextjs_shop.common.exception;

public class NotFoundException extends BusinessException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public NotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
    public NotFoundException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
