package clabi.poc.domain.chat.dto.chat_memo.req;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Schema(description = "채팅 메모 업데이트 요청 DTO")
public record UpdateChatMemoReqDto(
        @NotNull
        @Schema(description = "채팅 메모의 고유 ID", example = "1")
        int memoId,

        @NotBlank
        @NotNull
        @Schema(description = "변경할 메모 내용", example = "클라비 최고")
        String memoContent
) {
}
