package clabi.poc.domain.chat.controller;

import clabi.poc.domain.chat.dto.chat_satisfaction.req.UpdateChatSatisfactionReqDto;
import clabi.poc.domain.chat.dto.chat_satisfaction.res.ChatSatisfactionDto;
import clabi.poc.domain.chat.dto.chat_satisfaction.req.CreateChatSatisfactionReqDto;
import clabi.poc.domain.chat.entity.ChatSatisfaction;
import clabi.poc.domain.chat.service.ChatSatisfactionService;
import clabi.poc.global.exception.GlobalExceptionHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Chat Satisfaction", description = "채팅 만족도 API")
@RestController
@RequestMapping("/api/v1/chat/satisfaction")
public class ChatSatisfactionController {

    private final ChatSatisfactionService chatSatisfactionService;

    public ChatSatisfactionController(ChatSatisfactionService chatSatisfactionService) {
        this.chatSatisfactionService = chatSatisfactionService;
    }

    @PostMapping
    @Operation(summary = "Create a new chat satisfaction entry", description = "새로운 채팅 만족도 정보를 저장하는 API 입니다.")
    @ApiResponse(
            responseCode = "201",
            description = "채팅 만족도 생성 성공",
            content = @Content(schema = @Schema(implementation = ChatSatisfactionDto.class))
    )
    @ApiResponse(
            responseCode = "400",
            description = "잘못된 요청",
            content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))
    )
    @ApiResponse(
            responseCode = "409",
            description = "이미 등록된 만족도",
            content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))
    )
    public ResponseEntity<ChatSatisfactionDto> createChatSatisfaction(
            @RequestBody @Valid CreateChatSatisfactionReqDto createChatSatisfactionReqDto) {

        ChatSatisfaction savedChatSatisfaction = chatSatisfactionService.saveChatSatisfaction(createChatSatisfactionReqDto);
        ChatSatisfactionDto responseDto = chatSatisfactionService.toChatSatisfactionDto(savedChatSatisfaction);
        return ResponseEntity.status(201).body(responseDto);
    }

    @PutMapping
    @Operation(summary = "Update a chat satisfaction entry", description = "채팅 만족도 정보를 수정하는 API 입니다.")
    @ApiResponse(
            responseCode = "200",
            description = "채팅 만족도 업데이트 성공",
            content = @Content(schema = @Schema(implementation = ChatSatisfactionDto.class))
    )
    @ApiResponse(
            responseCode = "400",
            description = "잘못된 요청",
            content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))
    )
    public ResponseEntity<ChatSatisfactionDto> updateChatSatisfaction(
            @RequestBody @Valid UpdateChatSatisfactionReqDto updateChatSatisfactionReqDto) {

        ChatSatisfaction updatedChatSatisfaction = chatSatisfactionService.updateChatSatisfaction(updateChatSatisfactionReqDto);
        ChatSatisfactionDto responseDto = chatSatisfactionService.toChatSatisfactionDto(updatedChatSatisfaction);
        return ResponseEntity.status(200).body(responseDto);
    }
}
