package clabi.poc.domain.chat.service;

import clabi.poc.domain.chat.dto.chat.res.ChatDto;
import clabi.poc.domain.chat.dto.chat_group.req.CreateChatGroupReqDto;
import clabi.poc.domain.chat.dto.chat_group.req.UpdateChatGroupReqDto;
import clabi.poc.domain.chat.dto.chat_group.res.ChatGroupDto;
import clabi.poc.domain.chat.dto.chat_group.res.ChatGroupGetDto;
import clabi.poc.domain.chat.dto.chat_group.res.ChatShareDto;
import clabi.poc.domain.chat.entity.ChatGroup;
import clabi.poc.domain.chat.mapper.ChatGroupMapper;
import clabi.poc.domain.chat.mapper.ChatMapper;
import clabi.poc.domain.chat.repository.ChatGroupRepository;
import clabi.poc.domain.common.enums.ErrorCode;
import clabi.poc.domain.util.SecurityUtil;
import clabi.poc.global.exception.RestApiException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ChatGroupService {

    private final ChatGroupRepository chatGroupRepository;
    private final ChatGroupMapper chatGroupMapper;
    private final ChatMapper chatMapper;

    public ChatGroupService(ChatGroupRepository chatGroupRepository, ChatGroupMapper chatGroupMapper, ChatMapper chatMapper) {
        this.chatGroupRepository = chatGroupRepository;
        this.chatGroupMapper = chatGroupMapper;
        this.chatMapper = chatMapper;
    }

    public List<ChatGroupGetDto> getChatGroup() {
        // security에 등록된 user id 조회
        String userId = SecurityUtil.getCurrentUserId();
        LocalDateTime after = LocalDate.now().minusDays(7).atStartOfDay();

        List<ChatGroup> chatGroups = chatGroupRepository.findAllByCreatedByAndCreatedAtAfterOrderByCreatedAtDesc(userId, after);

        return chatGroups.stream()
                .map(ChatGroupGetDto::toDto)
                .toList();
    }

    @Transactional
    public ChatGroup saveChatGroup(CreateChatGroupReqDto createChatGroupReqDto) {
        // security에 등록된 user id 조회
        String userId = SecurityUtil.getCurrentUserId();

        ChatGroup chatGroup = ChatGroup.builder()
                .title(createChatGroupReqDto.title())
                .createdBy(userId)
                .build();

        return chatGroupRepository.save(chatGroup);
    }

    @Transactional
    public ChatGroup updateChatGroupTitle(UpdateChatGroupReqDto updateChatGroupReqDto) {
        ChatGroup existingChat = chatGroupRepository.findById(updateChatGroupReqDto.chatGroupId())
                .orElseThrow(() -> new RestApiException(ErrorCode.ENTITY_NOT_FOUND));

        existingChat.updateChatGroupDetails(updateChatGroupReqDto);

        return chatGroupRepository.save(existingChat);
    }

    @Transactional
    public void deleteChatGroup(int id) {

        ChatGroup existingGroup = chatGroupRepository.findById(id)
                .orElseThrow(() -> new RestApiException(ErrorCode.ENTITY_NOT_FOUND));

        chatGroupRepository.delete(existingGroup);
    }

    public ChatGroupDto toChatGroupDto(ChatGroup chatGroup) {
        return chatGroupMapper.toChatGroupDto(chatGroup);
    }

    public ChatShareDto createSharePathParam(int groupId) {
        LocalDateTime currentTime = LocalDateTime.now();
        String formattedTime = currentTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        // groupId 시간 정보를 하나의 문자열로 결합 후 Base64 인코딩
        String dataToEncode = groupId + "#" + formattedTime;
        String base64Encoded = Base64.getEncoder().encodeToString(dataToEncode.getBytes());

        return new ChatShareDto(base64Encoded);
    }

    @Transactional
    public ChatGroupDto getChatGroupByEncodedData(String encodedData) {
        // Base64 디코딩
        byte[] decodedBytes = Base64.getDecoder().decode(encodedData);
        String decodedString = new String(decodedBytes);

        // ":" 구분자로 groupId와 timestamp 분리
        String[] parts = decodedString.split("#");
        if (parts.length != 2) {
            throw new RestApiException(ErrorCode.INVALID_ARGUMENT);
        }

        int groupId = Integer.parseInt(parts[0]);
        LocalDateTime timestamp = LocalDateTime.parse(parts[1], DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        // groupId로 채팅 그룹 조회
        ChatGroup chatGroup = chatGroupRepository.findById(groupId)
                .orElseThrow(() -> new RestApiException(ErrorCode.ENTITY_NOT_FOUND));

        // timestamp 이전에 생성된 chat만 필터링
        List<ChatDto> filteredChats = chatGroup.getChats().stream()
                .filter(chat -> chat.getUpdatedAt().isBefore(timestamp))
                .map(chatMapper::toChatDto)
                .collect(Collectors.toList());

        // 새로운 ChatGroupDto 생성하여 반환
        return new ChatGroupDto(
                chatGroup.getChatGroupId(),
                chatGroup.getTitle(),
                filteredChats,
                chatGroup.getCreatedAt(),
                chatGroup.getUpdatedAt()
        );
    }


}
