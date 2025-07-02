package clabi.poc.domain.chat.dto.chat_group.res;

import clabi.poc.domain.chat.dto.chat.res.ChatDto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Schema(description = "채팅 그룹 응답 DTO")
public record ChatGroupDto(
        @Schema(description = "채팅 그룹의 고유 ID", example = "1")
        Integer chatGroupId,

        @NotBlank
        @Schema(description = "채팅 그룹의 제목", example = "클라비는 어디에 위치하고 있나요?")
        String title,

        @Schema(description = "그룹 내 채팅 목록")
        List<ChatDto> chats,

        @Schema(description = "그룹 생성 날짜 및 시간", example = "2024-10-24")
        LocalDateTime createdAt,

        @Schema(description = "그룹 마지막 업데이트 날짜 및 시간", example = "2024-10-25")
        LocalDateTime updatedAt
) {

}
