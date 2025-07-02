package clabi.poc.domain.setting.dto.res;

import clabi.poc.domain.setting.entity.ExampleQuestionCategory;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.models.examples.Example;
import lombok.Builder;

@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Schema(description = "예제 질문 카테고리 DTO")
public record ExampleQuestionCategoryDto(

        @Schema(description = "카테고리 ID", example = "1")
        Integer id,

        @Schema(description = "카테고리명", example = "핵심 사업별 사업 유형")
        String categoryName,

        @Schema(description = "카테고리 코드", example = "CategorizationSelectAction")
        String code
) {
        public static ExampleQuestionCategoryDto from(ExampleQuestionCategory category) {
                return ExampleQuestionCategoryDto.builder()
                        .id(category.getId())
                        .categoryName(category.getName())
                        .build();
        }
}
