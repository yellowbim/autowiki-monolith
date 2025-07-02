package clabi.poc.domain.chat.service;

import clabi.poc.domain.chat.dto.chat_satisfaction.req.UpdateChatSatisfactionReqDto;
import clabi.poc.domain.chat.dto.chat_satisfaction.res.ChatSatisfactionDto;
import clabi.poc.domain.chat.dto.chat_satisfaction.req.CreateChatSatisfactionReqDto;
import clabi.poc.domain.chat.entity.Chat;
import clabi.poc.domain.chat.entity.ChatSatisfaction;
import clabi.poc.domain.chat.mapper.ChatSatisfactionMapper;
import clabi.poc.domain.chat.repository.ChatRepository;
import clabi.poc.domain.chat.repository.ChatSatisfactionRepository;
import clabi.poc.domain.common.enums.ErrorCode;
import clabi.poc.global.exception.RestApiException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ChatSatisfactionService {

    private final ChatRepository chatRepository;
    private final ChatSatisfactionRepository chatSatisfactionRepository;
    private final ChatSatisfactionMapper chatSatisfactionMapper;

    public ChatSatisfactionService(ChatRepository chatRepository, ChatSatisfactionRepository chatSatisfactionRepository, ChatSatisfactionMapper chatSatisfactionMapper) {
        this.chatRepository = chatRepository;
        this.chatSatisfactionRepository = chatSatisfactionRepository;
        this.chatSatisfactionMapper = chatSatisfactionMapper;
    }

    @Transactional
    public ChatSatisfaction saveChatSatisfaction(CreateChatSatisfactionReqDto createChatSatisfactionReqDto) {
        Chat chat = chatRepository.findById(createChatSatisfactionReqDto.chatId())
                .orElseThrow(() -> new RestApiException(ErrorCode.INVALID_ARGUMENT));

        if (chat.getChatSatisfaction() != null) {
            throw new RestApiException(ErrorCode.SATISTACTION_ALREADY_EXIST);
        }

        ChatSatisfaction chatSatisfaction = chatSatisfactionMapper.toChatSatisfactionEntityFromCreateChatSatisfactionReqDto(createChatSatisfactionReqDto);
        System.out.println("chatSatisfaction: " + chatSatisfaction);
        chatSatisfaction.associateChat(chat);

        return chatSatisfactionRepository.save(chatSatisfaction);
    }

    @Transactional
    public ChatSatisfaction updateChatSatisfaction(UpdateChatSatisfactionReqDto updateChatSatisfactionReqDto) {
        ChatSatisfaction existingChat = chatSatisfactionRepository.findById(updateChatSatisfactionReqDto.satisfactionId())
                .orElseThrow(() -> new RestApiException(ErrorCode.INVALID_ARGUMENT));

        existingChat.updateChatSatisfactionDetails(updateChatSatisfactionReqDto);

        return chatSatisfactionRepository.save(existingChat);
    }

    public ChatSatisfactionDto toChatSatisfactionDto(ChatSatisfaction chatSatisfaction) {
        return chatSatisfactionMapper.toChatSatisfactionDto(chatSatisfaction);
    }
}

