package clabi.poc.global.exception;

import clabi.poc.domain.common.enums.ErrorCode;
import lombok.Getter;

@Getter
public class RestApiException extends RuntimeException {
    private final ErrorCode errorCode; // 에러 코드 Enum
    private final String customMessage; // 사용자 정의 메시지 (선택 사항)

    // 기본 생성자
    public RestApiException(ErrorCode errorCode) {
        super(errorCode.getResultMessage());
        this.errorCode = errorCode;
        this.customMessage = null;
    }

    // HttpStatus 반환
    public org.springframework.http.HttpStatus getHttpStatus() {
        return this.errorCode.getResultHttpStatus();
    }
}