package clabi.poc.domain.chat.dto.chat_group.req;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record CreateChatShareReqDto(
        @NotNull
        @Schema(description = "채팅 그룹의 고유 ID", example = "1")
        int chatGroupId
) {
}
