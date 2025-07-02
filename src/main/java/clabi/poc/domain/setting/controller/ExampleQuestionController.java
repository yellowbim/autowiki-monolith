package clabi.poc.domain.setting.controller;

import clabi.poc.domain.setting.dto.res.ExampleQuestionCategoryDto;
import clabi.poc.domain.setting.dto.res.ExampleQuestionDto;
import clabi.poc.domain.setting.entity.ExampleQuestion;
import clabi.poc.domain.setting.service.ExampleQuestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "Setting", description = "예시 질문, 인사말, 프롬프트 입력 질문")
@RestController
@RequestMapping("/api/v1/example-question")
public class ExampleQuestionController {

    private final ExampleQuestionService exampleQuestionService;

    public ExampleQuestionController(ExampleQuestionService exampleQuestionService) {
        this.exampleQuestionService = exampleQuestionService;
    }

    @GetMapping("category")
    @Operation(summary = "Get Example Question Category List", description = "예시 질문 카테고리 목록 조회을 조회하는 API 입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "예시 질문 카테고리 목록 조회 성공하였습니다.",
                    content = @Content(schema = @Schema(implementation = ExampleQuestionCategoryDto.class)))
    })
    public ResponseEntity<Map<String, List<ExampleQuestionCategoryDto>>> getAllExampleQuestionCategories() {
        List<ExampleQuestionCategoryDto> categories = exampleQuestionService.getAllExampleQuestionCategories();
        Map<String, List<ExampleQuestionCategoryDto>> response = new HashMap<>();
        response.put("example_question_categories", categories);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Get Example Question List", description = "예시 질문 목록 조회을 조회하는 API 입니다.<br/>categoryId를 전달하지 않으면 통합조회입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "예시 질문 목록 조회 성공하였습니다.",
                    content = @Content(schema = @Schema(implementation = ExampleQuestion.class)))
    })
    public ResponseEntity<Map<String, List<ExampleQuestionDto>>> getAllExampleQuestions(@RequestParam(value = "category_id", required = false) Integer categoryId) {
        List<ExampleQuestionDto> questions = exampleQuestionService.getAllExampleQuestions(categoryId);
        Map<String, List<ExampleQuestionDto>> response = new HashMap<>();
        response.put("example_questions", questions);
        return ResponseEntity.ok(response);
    }
}
