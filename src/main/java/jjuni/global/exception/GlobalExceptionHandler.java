package jjuni.global.exception;

import jjuni.domain.common.enums.ErrorCode;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    // API 내부 에러
    @ExceptionHandler(RestApiException.class)
    public ResponseEntity<Object> handleRestApiException(RestApiException ex) {
        return ResponseEntity.status(ex.getErrorCode().getHttpStatus()).body(new ErrorResponse(ex.getErrorCode().getErrorCode(), ex.getMessage()));
    }

    // Handle JWT Access Token Expired Handler
    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<Object> handleExpiredJwtExceptions(ExpiredJwtException ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(ErrorCode.REFRESH_TOKEN_EXPIRED.getErrorCode(), ErrorCode.REFRESH_TOKEN_EXPIRED.getMessageKey()));
    }

    // Handle JWT Exception Handler
    @ExceptionHandler(JwtException.class)
    public ResponseEntity<Object> handleJwtExceptions(JwtException ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(ErrorCode.TOKEN_INVALID.getErrorCode(), ErrorCode.TOKEN_INVALID.getMessageKey()));
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(ErrorCode.PARAM_NOT_VALID.getErrorCode(), errors.toString()));
    }




    public static class ErrorResponse {
        private final Integer errorCode;
        private final String message;

        public ErrorResponse(Integer errorCode, String message) {
            this.errorCode = errorCode;
            this.message = message;
        }

        public Integer getErrorCode() {
            return errorCode;
        }

        public String getMessage() {
            return message;
        }
    }

}
