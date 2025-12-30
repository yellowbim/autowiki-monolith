package jjuni.domain.common.dto;

import jjuni.domain.common.enums.ErrorCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ApiResponse<T> {

    private boolean success;
    private Integer errorCode;
    private String message;
    private T data;

    private ApiResponse(boolean success, Integer errorCode, String message, T data) {
        this.success = success;
        this.errorCode = errorCode;
        this.message = message;
        this.data = data;
    }

    /** 성공: data 포함 */
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, null, null, data);
    }

    /** 성공: data 없음 */
    public static ApiResponse<Void> success() {
        return new ApiResponse<>(true, null, null, null);
    }

    /** 실패: ErrorCode 기반 */
    public static ApiResponse<Void> fail(ErrorCode errorCode) {
        return new ApiResponse<>(
                false,
                errorCode.getErrorCode(),
                errorCode.getMessageKey(),
                null
        );
    }

    /** 실패: 커스텀 메시지 필요할 때 (ex. validation 상세) */
    public static ApiResponse<Void> fail(ErrorCode errorCode, String customMessage) {
        return new ApiResponse<>(
                false,
                errorCode.getErrorCode(),
                customMessage,
                null
        );
    }

}
