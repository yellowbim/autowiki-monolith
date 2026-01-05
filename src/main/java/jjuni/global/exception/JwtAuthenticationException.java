package jjuni.global.exception;

import jjuni.domain.common.enums.ErrorCode;
import lombok.Getter;
import org.springframework.security.core.AuthenticationException;

/**
 * JWT 인증 예외
 */
@Getter
public class JwtAuthenticationException extends AuthenticationException {
    private final ErrorCode errorCode;

    public JwtAuthenticationException(ErrorCode errorCode) {
        super(errorCode.getMessageKey());
        this.errorCode = errorCode;
    }

}
