package clabi.poc.domain.chat.dto.chat_group.res;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record ChatShareDto(
        @NotNull
        @Schema(description = "공유 URL에 사용되는 encodedData", example = "MToyMDI0LTExLTA3VDE0OjAyOjE2LjMxMTMwODU0NA==")
        String encodedData
) {
}
