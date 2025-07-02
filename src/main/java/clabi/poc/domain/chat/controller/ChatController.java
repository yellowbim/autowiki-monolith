package clabi.poc.domain.chat.controller;

import clabi.poc.domain.chat.dto.chat.req.UpdateChatReqDto;
import clabi.poc.domain.chat.dto.chat.res.ChatDto;
import clabi.poc.domain.chat.dto.chat.req.CreateChatReqDto;
import clabi.poc.domain.chat.dto.chat.res.ChatWithSatisfactionDto;
import clabi.poc.domain.chat.entity.Chat;
import clabi.poc.domain.chat.service.ChatService;
import clabi.poc.domain.stats.service.StatsDailyService;
import clabi.poc.global.exception.GlobalExceptionHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Chat Room", description = "채팅방 API")
@RestController
@RequestMapping("/api/v1/chat")
public class ChatController {

    private final ChatService chatService;
    private final StatsDailyService statsDailyService;

    public ChatController(ChatService chatService, StatsDailyService statsDailyService) {
        this.chatService = chatService;
        this.statsDailyService = statsDailyService;
    }

    @GetMapping
    @Operation(summary = "Get chat", description = "과거 채팅 내용을 조회하는 API 입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "채팅 조회 성공", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ChatWithSatisfactionDto.class)))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))
            )
    })
    public ResponseEntity<List<ChatWithSatisfactionDto>> getChat(@RequestParam(name = "group_id", required = true) Integer groupId) {
        List<ChatWithSatisfactionDto> responseDto = chatService.getChat(groupId);;
        return ResponseEntity.status(201).body(responseDto);
    }

    @PostMapping
    @Operation(summary = "Create chat", description = "채팅 내용을 저장하는 API 입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "채팅 생성 성공",
                    content = @Content(schema = @Schema(implementation = ChatDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))
            )
    })
    public ResponseEntity<ChatDto> createChat(@RequestBody @Valid CreateChatReqDto createChatReqDto) {
        if (createChatReqDto.useTokenCount() != null) {
            statsDailyService.updateStatsDaily(createChatReqDto.useTokenCount());
        }

        Chat savedChat = chatService.saveChat(createChatReqDto);
        ChatDto responseDto = chatService.getChatDto(savedChat);
        return ResponseEntity.status(201).body(responseDto);
    }

    @PutMapping
    @Operation(summary = "Update chat", description = "AI 답변 도중 중단되어 고객이 재질의를 하게되면, 이전 채팅을 수정하는 API 입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "채팅 수정 성공",
                    content = @Content(schema = @Schema(implementation = ChatDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))
            )
    })
    public ResponseEntity<ChatDto> updateChat(@RequestBody @Valid UpdateChatReqDto updateChatReqDto) {
        if (updateChatReqDto.useTokenCount() != null) {
            statsDailyService.updateStatsDaily(updateChatReqDto.useTokenCount());
        }

        Chat updatedChat = chatService.updateChat(updateChatReqDto);
        ChatDto responseDto = chatService.getChatDto(updatedChat);
        return ResponseEntity.ok(responseDto);
    }
}
