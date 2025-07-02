package clabi.poc.domain.chat.dto.chat_memo.req;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Schema(description = "채팅 메모 생성 요청 DTO")
public record CreateChatMemoReqDto(
        @NotNull
        @Schema(description = "채팅 ID", example = "1")
        int chatId,

        @NotBlank
        @NotNull
        @Schema(description = "메모 내용", example = "클라비 최고")
        String memoContent
) {
}
