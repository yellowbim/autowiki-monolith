package clabi.poc.domain.chat.dto.chat_group.req;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Schema(description = "채팅 그룹 업데이트 요청 DTO")
public record UpdateChatGroupReqDto (
        @NotNull
        @Schema(description = "채팅 그룹의 고유 ID", example = "1")
        int chatGroupId,

        @NotBlank
        @Schema(description = "변경된 채팅 그룹의 제목", example = "클라비는 어디에 위치하고 있나요?")
        String title
) {
}
