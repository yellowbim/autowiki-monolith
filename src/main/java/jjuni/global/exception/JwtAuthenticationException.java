package jjuni.global.exception;

import jjuni.domain.common.enums.ErrorCode;
import org.springframework.security.core.AuthenticationException;

/**
 * JWT 인증 예외
 */
public class JwtAuthenticationException extends AuthenticationException {
    private final ErrorCode errorCode;

    public JwtAuthenticationException(ErrorCode errorCode) {
        super(errorCode.name());
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
