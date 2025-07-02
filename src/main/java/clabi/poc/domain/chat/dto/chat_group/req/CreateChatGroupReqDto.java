package clabi.poc.domain.chat.dto.chat_group.req;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Schema(description = "채팅 그룹 생성 요청 DTO")
public record CreateChatGroupReqDto(
        @NotBlank
        @Schema(description = "첫 번째 채팅 질문", example = "클라비는 어디에 위치하고 있나요?")
        String title
) {
}
