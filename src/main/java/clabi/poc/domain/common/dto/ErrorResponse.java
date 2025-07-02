package clabi.poc.domain.common.dto;

import clabi.poc.domain.common.enums.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * JWT 관련 특정 에러 필터
 */
@Getter
@AllArgsConstructor
public class ErrorResponse {
    private final Integer errorCode;
    private final String message;

    public static ErrorResponse from(ErrorCode errorCode) {
        return new ErrorResponse(errorCode.getErrorCode(), errorCode.getResultMessage());
    }
}

