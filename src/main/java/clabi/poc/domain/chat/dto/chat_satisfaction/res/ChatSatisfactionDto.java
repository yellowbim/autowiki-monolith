package clabi.poc.domain.chat.dto.chat_satisfaction.res;

import clabi.poc.domain.chat.enums.SatisfactionType;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Schema(description = "채팅 만족도 응답 DTO")
public record ChatSatisfactionDto(
        @NotNull
        @Schema(description = "채팅 만족도의 고유 ID", example = "1")
        Integer satisfactionId,

        @NotBlank
        @NotNull
        @Schema(description = "만족도 내용 (예: '더 자세하게 설명해주세요.')", example = "더 자세하게 설명해주세요.")
        String satisfactionContent,

        @NotNull
        @Schema(description = "연관된 채팅의 ID", example = "1")
        int chatId,

        @Schema(description = "좋아요, 싫어요", example = "LIKE")
        SatisfactionType satisfactionType
) {
}
