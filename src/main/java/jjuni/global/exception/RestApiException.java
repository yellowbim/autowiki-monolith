package jjuni.global.exception;

import jjuni.domain.common.enums.ErrorCode;
import lombok.Getter;

@Getter
public class RestApiException extends RuntimeException {
    private final ErrorCode errorCode; // 에러 코드 Enum

    // 기본 생성자
    public RestApiException(ErrorCode errorCode) {
        super(errorCode.getMessageKey()); // 로그용 (의미만)
        this.errorCode = errorCode;
    }
}