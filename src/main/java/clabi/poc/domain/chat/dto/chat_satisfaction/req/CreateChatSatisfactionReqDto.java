package clabi.poc.domain.chat.dto.chat_satisfaction.req;

import clabi.poc.domain.chat.enums.SatisfactionType;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Schema(description = "만족도 생성 요청을 위한 DTO")
public record CreateChatSatisfactionReqDto(

        @NotNull
        @Schema(description = "연관된 채팅의 ID", example = "1")
        int chatId,

        @Schema(description = "만족도 내용 (예: '싫어요')", example = "더 자세하게 설명해주세요.")
        String satisfactionContent,

        @Schema(description = "좋아요, 싫어요", example = "LIKE")
        SatisfactionType satisfactionType
) {
}
