package clabi.poc.domain.setting.controller;

import clabi.poc.domain.setting.entity.PromptInput;
import clabi.poc.domain.setting.service.PromptInputService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Setting", description = "샘플 질문, 인사말, 프롬프트 입력 질문")
@RestController
@RequestMapping("/api/v1/prompt-input")
public class PromptInputController {
    private final PromptInputService promptInputService;

    public PromptInputController(PromptInputService promptInputService) {
        this.promptInputService = promptInputService;
    }

    @GetMapping
    @Operation(summary = "Get Prompt Input", description = "프롬프트 입력을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "프롬프트 입력 조회 성공",
                    content = @Content(schema = @Schema(implementation = PromptInput.class)))
    })
    public ResponseEntity<List<PromptInput>> getAllPromptInputs() {
        List<PromptInput> promptInput = promptInputService.getPromptInput();
        return ResponseEntity.ok(promptInput);
    }
}
