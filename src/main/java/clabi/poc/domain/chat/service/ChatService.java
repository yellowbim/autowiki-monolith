package clabi.poc.domain.chat.service;

import clabi.poc.domain.chat.dto.chat.req.UpdateChatReqDto;
import clabi.poc.domain.chat.dto.chat.res.ChatDto;
import clabi.poc.domain.chat.dto.chat.req.CreateChatReqDto;
import clabi.poc.domain.chat.dto.chat.res.ChatWithSatisfactionDto;
import clabi.poc.domain.chat.entity.Chat;
import clabi.poc.domain.chat.entity.ChatGroup;
import clabi.poc.domain.chat.repository.ChatGroupRepository;
import clabi.poc.domain.chat.repository.ChatRepository;
import clabi.poc.domain.chat.mapper.ChatMapper;
import clabi.poc.domain.common.enums.ErrorCode;
import clabi.poc.global.exception.RestApiException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ChatService {

    private final ChatGroupRepository chatGroupRepository;
    private final ChatRepository chatRepository;
    private final ChatMapper chatMapper;

    public ChatService(ChatGroupRepository chatGroupRepository, ChatRepository chatRepository, ChatMapper chatMapper) {
        this.chatGroupRepository = chatGroupRepository;
        this.chatRepository = chatRepository;
        this.chatMapper = chatMapper;
    }

    @Transactional
    public List<ChatWithSatisfactionDto> getChat(Integer groupId) {
        List<Chat> chats = chatRepository.findAllByChatGroup_ChatGroupIdOrderByChatIdAsc(groupId);
        return chatMapper.toChatWithSatisfactionDtoList(chats);
    }

    @Transactional
    public Chat saveChat(CreateChatReqDto createChatReqDto) {
        ChatGroup chatGroup = chatGroupRepository.findById(createChatReqDto.chatGroupId())
                .orElseThrow(() -> new RestApiException(ErrorCode.INVALID_ARGUMENT));

        Chat chat = chatMapper.toChatEntityFromCreateChatReqDto(createChatReqDto);
        chat.associateChatGroup(chatGroup);

        return chatRepository.save(chat);
    }

    @Transactional
    public Chat updateChat(UpdateChatReqDto updateChatReqDto) {
        Chat existingChat = chatRepository.findById(updateChatReqDto.chatId())
                .orElseThrow(() -> new RestApiException(ErrorCode.ENTITY_NOT_FOUND));

        existingChat.updateChatDetails(updateChatReqDto);

        return chatRepository.save(existingChat);
    }

    public ChatDto getChatDto(Chat chat) {
        return chatMapper.toChatDto(chat);
    }
}
