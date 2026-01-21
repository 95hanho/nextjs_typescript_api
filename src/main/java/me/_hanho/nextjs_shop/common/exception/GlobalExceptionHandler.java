package me._hanho.nextjs_shop.common.exception;

import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ServletRequestBindingException.class)
	public ResponseEntity<Map<String, Object>> handleBinding(ServletRequestBindingException e) {
		 System.out.println("### handleBinding HIT: " + e.getMessage());
	    String msg = String.valueOf(e.getMessage());

	    // request attribute 누락만 인증으로 처리
	    if (msg.contains("Missing request attribute") &&
	        (msg.contains("'sellerNo'") || msg.contains("'userNo'") || msg.contains("'adminNo'") || msg.contains("'phoneUserId'"))) {
	        return ResponseEntity
	                .status(ErrorCode.UNAUTHORIZED_TOKEN.getStatus())
	                .body(Map.of("message", ErrorCode.UNAUTHORIZED_TOKEN.getCode()));
	    }

	    // 그 외 바인딩 문제는 400
	    return ResponseEntity
	            .badRequest()
	            .body(Map.of("message", "BAD_REQUEST"));
	}

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Map<String, Object>> handleBusinessException(BusinessException e) {
        ErrorCode code = e.getErrorCode();
        return ResponseEntity
                .status(code.getStatus())
                .body(Map.of(
                        "message", code.getCode(),          // 프론트/BFF용
                        "detail", e.getMessage()            // 로그/디버그용(싫으면 빼도 됨)
                ));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException e) {
        return ResponseEntity
                .badRequest()
                .body(Map.of("message", "VALIDATION_ERROR"));
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<Map<String, Object>> handleDataAccess(DataAccessException e) {
        return ResponseEntity
                .status(500)
                .body(Map.of("message", ErrorCode.DB_ERROR.getCode()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleException(Exception e) {
        return ResponseEntity
                .status(500)
                .body(Map.of("message", "SERVER_ERROR"));
    }
}
