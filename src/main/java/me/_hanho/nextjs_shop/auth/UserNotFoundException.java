package me._hanho.nextjs_shop.auth;

import me._hanho.nextjs_shop.common.exception.ErrorCode;
import me._hanho.nextjs_shop.common.exception.NotFoundException;

public class UserNotFoundException extends NotFoundException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public UserNotFoundException(Long userId) {
        super(ErrorCode.USER_NOT_FOUND, "User not found: " + userId);
    }
    public UserNotFoundException(String message) {
        super(ErrorCode.USER_NOT_FOUND, message);
    }
}