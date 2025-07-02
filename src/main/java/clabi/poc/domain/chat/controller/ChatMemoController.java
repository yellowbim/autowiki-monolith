package clabi.poc.domain.chat.controller;

import clabi.poc.domain.chat.dto.chat_memo.req.UpdateChatMemoReqDto;
import clabi.poc.domain.chat.dto.chat_memo.res.ChatMemoDto;
import clabi.poc.domain.chat.dto.chat_memo.req.CreateChatMemoReqDto;
import clabi.poc.domain.chat.entity.ChatMemo;
import clabi.poc.domain.chat.service.ChatMemoService;
import clabi.poc.global.exception.GlobalExceptionHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Chat Memo", description = "채팅 메모 API")
@RestController
@RequestMapping("/api/v1/chat/memo")
public class ChatMemoController {

    private final ChatMemoService chatMemoService;

    public ChatMemoController(ChatMemoService chatMemoService) {
        this.chatMemoService = chatMemoService;
    }

    @PostMapping
    @Operation(summary = "Create a new chat memo", description = "새로운 채팅 메모를 생성하는 API 입니다.")
    @ApiResponse(
            responseCode = "201",
            description = "메모 생성 성공",
            content = @Content(schema = @Schema(implementation = ChatMemoDto.class))
    )
    @ApiResponse(
            responseCode = "400",
            description = "잘못된 요청",
            content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))
    )
    public ResponseEntity<ChatMemoDto> createChatMemo(@RequestBody @Valid CreateChatMemoReqDto createChatMemoReqDto) {
        ChatMemo savedChatMemo = chatMemoService.saveChatMemo(createChatMemoReqDto);
        ChatMemoDto responseDto = chatMemoService.toChatMemoDto(savedChatMemo);
        return ResponseEntity.status(201).body(responseDto);
    }

    @PutMapping
    @Operation(summary = "Update a chat memo", description = "채팅 메모를 수정하는 API 입니다.")
    @ApiResponse(
            responseCode = "200",
            description = "메모 업데이트 성공",
            content = @Content(schema = @Schema(implementation = ChatMemoDto.class))
    )
    @ApiResponse(
            responseCode = "400",
            description = "잘못된 요청",
            content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))
    )
    public ResponseEntity<ChatMemoDto> updateChatMemo(@RequestBody @Valid UpdateChatMemoReqDto updateChatMemoReqDto) {
        ChatMemo updatedChatMemo = chatMemoService.updateChatMemo(updateChatMemoReqDto);
        ChatMemoDto responseDto = chatMemoService.toChatMemoDto(updatedChatMemo);
        return ResponseEntity.status(200).body(responseDto);
    }

    @DeleteMapping
    @Operation(summary = "Delete a chat memo", description = "채팅 메모를 삭제하는 API 입니다.")
    @ApiResponse(
            responseCode = "204",
            description = "메모 삭제 성공"
    )
    @ApiResponse(
            responseCode = "400",
            description = "잘못된 요청",
            content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))
    )
    public ResponseEntity<Void> deleteChatMemo(@RequestParam int chat_id) {
        chatMemoService.deleteChatMemo(chat_id);
        return ResponseEntity.noContent().build();
    }
}
