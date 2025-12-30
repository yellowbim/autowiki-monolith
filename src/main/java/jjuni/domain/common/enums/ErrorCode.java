package jjuni.domain.common.enums;

import org.springframework.http.HttpStatus;

/**
 * ErrorCode 규칙
 *
 * 1000 ~ 1999 : COMMON / SYSTEM
 * 2000 ~ 2999 : AUTH (LOGIN / AUTHORIZATION)
 * 3000 ~ 3999 : TOKEN / JWT
 * 4000 ~ 4999 : USER
 * 5000 ~ 5999 : PARAM / VALIDATION
 * 6000 ~ 6999 : STATS
 * 7000 ~ 7999 : CHAT
 */
public enum ErrorCode {
    UNAUTHORIZED(1000, "error.common.unauthorized", HttpStatus.UNAUTHORIZED),
    INVALID_ARGUMENT(1001, "error.common.invalid.argument", HttpStatus.BAD_REQUEST),
    INTERNAL_ERROR(1003, "error.common.internal", HttpStatus.INTERNAL_SERVER_ERROR),

    AUTH_INVALID_CREDENTIAL(2000, "error.auth.invalid.credential", HttpStatus.BAD_REQUEST),
    AUTH_USER_NOT_FOUND(2001, "error.auth.user.not.found", HttpStatus.NOT_FOUND),

    TOKEN_NOT_FOUND(3000, "error.token.not.found", HttpStatus.UNAUTHORIZED),
    TOKEN_PARSING_ERROR(3001, "error.token.parsing", HttpStatus.BAD_REQUEST),
    ACCESS_TOKEN_EXPIRED(3002, "error.token.access.expired", HttpStatus.UNAUTHORIZED),
    REFRESH_TOKEN_EXPIRED(3003, "error.token.refresh.expired", HttpStatus.UNAUTHORIZED),
    TOKEN_INVALID(3004, "error.token.invalid", HttpStatus.BAD_REQUEST),
    TOKEN_BLACKLISTED(3005, "error.token.blacklisted", HttpStatus.UNAUTHORIZED),

    USER_NOT_FOUND(4000, "error.user.not.found", HttpStatus.NOT_FOUND),
    USER_ALREADY_EXISTS(4001, "error.user.already.exists", HttpStatus.CONFLICT),

    PARAM_NOT_VALID(5000, "error.param.not.valid", HttpStatus.BAD_REQUEST),

    STATS_NOT_FOUND(6000, "error.stats.not.found", HttpStatus.NOT_FOUND),
    STATS_ALREADY_EXISTS(6001, "error.stats.already.exists", HttpStatus.CONFLICT),

    CHAT_MEMO_ALREADY_EXISTS(7000, "error.chat.memo.already.exists", HttpStatus.CONFLICT),
    CHAT_SATISFACTION_ALREADY_EXISTS(7001, "error.chat.satisfaction.already.exists", HttpStatus.CONFLICT);

    private final int errorCode;
    private final String messageKey;
    private final HttpStatus httpStatus;

    ErrorCode(int errorCode, String messageKey, HttpStatus httpStatus) {
        this.errorCode = errorCode;
        this.messageKey = messageKey;
        this.httpStatus = httpStatus;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getMessageKey() {
        return messageKey;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
