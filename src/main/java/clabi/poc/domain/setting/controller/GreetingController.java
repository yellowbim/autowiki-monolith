package clabi.poc.domain.setting.controller;

import clabi.poc.domain.setting.entity.Greeting;
import clabi.poc.domain.setting.service.GreetingService;
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
@RequestMapping("/api/v1/greeting")
public class GreetingController {
    private final GreetingService greetingService;

    public GreetingController(GreetingService greetingService) {
        this.greetingService = greetingService;
    }

    @GetMapping
    @Operation(summary = "Get Greeting Message", description = "인사말을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "인사말 조회 성공",
                    content = @Content(schema = @Schema(implementation = Greeting.class)))
    })
    public ResponseEntity<List<Greeting>> getAllGreetings() {
        List<Greeting> greetings = greetingService.getGreeting();
        return ResponseEntity.ok(greetings);
    }
}
