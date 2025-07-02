package clabi.poc.domain.setting.dto.res;

import clabi.poc.domain.setting.entity.ExampleQuestion;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Schema(description = "예제 질문 DTO")
public record ExampleQuestionDto (

        @Schema(description = "질문 ID", example = "1")
        Integer id,

        @Schema(description = "예제 질문 카테고리 ID", example = "1")
        Integer categoryId,

        @Schema(description = "예제 질문 카테고리명", example = "핵심 사업별 사업 유형")
        String categoryName,

        @Schema(description = "예제 질문 카테고리 코드", example = "CategorizationSelectAction")
        String categoryCode,

        @Schema(description = "예제 질문", example = "예시 질문 입니다.")
        String example

) {
        public static ExampleQuestionDto from(ExampleQuestion questions) {
                return ExampleQuestionDto.builder()
                        .id(questions.getId())
                        .categoryId(questions.getCategory().getId())
                        .categoryName(questions.getCategory().getName())
                        .categoryCode(questions.getCategory().getCode())
                        .example(questions.getExample())
                        .build();
        }
}
