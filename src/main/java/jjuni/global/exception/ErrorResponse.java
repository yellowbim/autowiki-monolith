package jjuni.global.exception;

import jjuni.domain.common.enums.ErrorCode;
import jjuni.global.message.ErrorMessageResolver;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponse {
    private final int code;
    private final String message;

    public static ErrorResponse from(
            ErrorCode errorCode,
            ErrorMessageResolver resolver
    ) {
        return new ErrorResponse(
                errorCode.getErrorCode(),
                resolver.resolve(errorCode)
        );
    }

}
