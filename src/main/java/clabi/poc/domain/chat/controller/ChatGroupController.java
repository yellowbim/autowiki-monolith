package clabi.poc.domain.chat.controller;

import clabi.poc.domain.chat.dto.chat_group.req.UpdateChatGroupReqDto;
import clabi.poc.domain.chat.dto.chat_group.res.ChatGroupDto;
import clabi.poc.domain.chat.dto.chat_group.req.CreateChatGroupReqDto;
import clabi.poc.domain.chat.dto.chat_group.res.ChatGroupGetDto;
import clabi.poc.domain.chat.dto.chat_group.res.ChatShareDto;
import clabi.poc.domain.chat.entity.ChatGroup;
import clabi.poc.domain.chat.service.ChatGroupService;
import clabi.poc.global.exception.GlobalExceptionHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Chat Group", description = "채팅 그룹(화면 LNB 영역), 그룹 공유 API")
@RestController
@RequestMapping("/api/v1/chat/group")
public class ChatGroupController {

    private final ChatGroupService chatGroupService;

    public ChatGroupController(ChatGroupService chatGroupService) {
        this.chatGroupService = chatGroupService;
    }

    @GetMapping
    @Operation(summary = "Get user chat group history", description = "사용자 id로 채팅 그룹을 조회하는 API 입니다.<br/>조회 시 jwt에 있는 정보로 조회를 하게됩니다.<br/>오늘 ~ 7일(포함) 까지의 데이터만 조회됩니다.")
    @ApiResponse(responseCode = "200", description = "채팅 그룹 조회 성공", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ChatGroupGetDto.class))))
    @ApiResponse(
            responseCode = "400",
            description = "잘못된 요청",
            content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))
    )
    public ResponseEntity<List<ChatGroupGetDto>> createChatGroup() {
        List<ChatGroupGetDto> responseDto = chatGroupService.getChatGroup();
        return ResponseEntity.status(200).body(responseDto);
    }


    @PostMapping
    @Operation(summary = "Create a new chat group", description = "새로운 채팅 그룹을 생성하는 API 입니다.<br/><b>이후 Create chat API를 요청</b>해아합니다.")
    @ApiResponse(
            responseCode = "201",
            description = "채팅 그룹 생성 성공",
            content = @Content(schema = @Schema(implementation = ChatGroupDto.class))
    )
    @ApiResponse(
            responseCode = "400",
            description = "잘못된 요청",
            content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))
    )
    public ResponseEntity<ChatGroupDto> createChatGroup(
            @RequestBody @Valid CreateChatGroupReqDto createChatGroupReqDto) {
        ChatGroup savedChatGroup = chatGroupService.saveChatGroup(createChatGroupReqDto);
        ChatGroupDto responseDto = chatGroupService.toChatGroupDto(savedChatGroup);
        return ResponseEntity.status(201).body(responseDto);
    }

    @PutMapping
    @Operation(summary = "Update a chat group", description = "채팅 그룹 제목을 수정하는 API 입니다.")
    @ApiResponse(
            responseCode = "200",
            description = "채팅 그룹 업데이트 성공",
            content = @Content(schema = @Schema(implementation = ChatGroupDto.class))
    )
    @ApiResponse(
            responseCode = "400",
            description = "잘못된 요청",
            content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))
    )
    public ResponseEntity<ChatGroupDto> updateChatGroup(
            @RequestBody @Valid UpdateChatGroupReqDto updateChatGroupReqDto) {
        ChatGroup updatedChatGroup = chatGroupService.updateChatGroupTitle(updateChatGroupReqDto);
        ChatGroupDto responseDto = chatGroupService.toChatGroupDto(updatedChatGroup);
        return ResponseEntity.status(201).body(responseDto);
    }

    @DeleteMapping
    @Operation(summary = "Delete a chat group", description = "채팅 그룹을 삭제하는 API 입니다.")
    @ApiResponse(
            responseCode = "204",
            description = "채팅 그룹 삭제 성공"
    )
    @ApiResponse(
            responseCode = "400",
            description = "잘못된 요청",
            content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))
    )
    public ResponseEntity<Void> deleteChatGroup(@RequestParam int id) {
        chatGroupService.deleteChatGroup(id);
        return ResponseEntity.noContent().build();
    }

    // group_id와 현재 시간을 Base64로 인코딩하여 반환하는 API
    @GetMapping("/share")
    @Operation(summary = "Create Share Encoded_data", description = "채팅 그룹의 고유 ID를 받아 시간과 함께 Base64로 인코딩하여 반환하는 API 입니다.")
    @ApiResponse(
            responseCode = "200",
            description = "encoded_data 생성 성공",
            content = @Content(schema = @Schema(implementation = ChatShareDto.class))
    )
    @ApiResponse(
            responseCode = "400",
            description = "잘못된 요청",
            content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))
    )
    public ResponseEntity<ChatShareDto> createSharePathParam(@RequestParam("groupId") int groupId) {
        ChatShareDto responseDto = chatGroupService.createSharePathParam(groupId);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/share/{encodedData}")
    @Operation(summary = "Get Shared Chat Group Data", description = "공유받은 url로 들어오면 encoded_data 를 decoding 하여 채팅 그룹과 채팅정보를 내려주는 API 입니다.<br/>만족도 정보는 포함되지 않습니다.")
    @ApiResponse(
            responseCode = "200",
            description = "공유 채팅 그룹 데이터 조회 성공",
            content = @Content(schema = @Schema(implementation = ChatGroupDto.class))
    )
    @ApiResponse(
            responseCode = "400",
            description = "잘못된 요청",
            content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))
    )
    public ResponseEntity<ChatGroupDto> getChatGroupByEncodedData(@PathVariable("encodedData") String encodedData) {
        ChatGroupDto responseDto = chatGroupService.getChatGroupByEncodedData(encodedData);
        return ResponseEntity.ok(responseDto);
    }
}
