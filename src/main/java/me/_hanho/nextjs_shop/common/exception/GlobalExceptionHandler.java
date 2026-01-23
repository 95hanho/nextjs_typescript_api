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
	public ResponseEntity<Map<String, Object>> handleBinding(org.springframework.web.bind.ServletRequestBindingException e) {
	    ErrorCode code = ErrorCode.BAD_REQUEST;
	    return ResponseEntity
	            .status(code.getStatus())
	            .body(Map.of("message", code.getCode(), "detail", code.getDefaultMessage()));
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
