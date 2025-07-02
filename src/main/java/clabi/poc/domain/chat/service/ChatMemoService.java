package clabi.poc.domain.chat.service;

import clabi.poc.domain.chat.dto.chat_memo.req.UpdateChatMemoReqDto;
import clabi.poc.domain.chat.dto.chat_memo.res.ChatMemoDto;
import clabi.poc.domain.chat.dto.chat_memo.req.CreateChatMemoReqDto;
import clabi.poc.domain.chat.entity.Chat;
import clabi.poc.domain.chat.entity.ChatMemo;
import clabi.poc.domain.chat.mapper.ChatMemoMapper;
import clabi.poc.domain.chat.repository.ChatMemoRepository;
import clabi.poc.domain.chat.repository.ChatRepository;
import clabi.poc.domain.common.enums.ErrorCode;
import clabi.poc.global.exception.RestApiException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ChatMemoService {

    private final ChatRepository chatRepository;
    private final ChatMemoRepository chatMemoRepository;
    private final ChatMemoMapper chatMemoMapper;

    public ChatMemoService(ChatRepository chatRepository, ChatMemoRepository chatMemoRepository, ChatMemoMapper chatMemoMapper) {
        this.chatRepository = chatRepository;
        this.chatMemoRepository = chatMemoRepository;
        this.chatMemoMapper = chatMemoMapper;
    }

    @Transactional
    public ChatMemo saveChatMemo(CreateChatMemoReqDto createChatMemoReqDto) {
        Chat chat = chatRepository.findById(createChatMemoReqDto.chatId())
                .orElseThrow(() -> new RestApiException(ErrorCode.INVALID_ARGUMENT));

        if (chat.getChatMemo() != null) {
            throw new RestApiException(ErrorCode.CHAT_MEMO_ALREADY_EXIST);
        }

        ChatMemo chatMemo = chatMemoMapper.toChatMemoEntityFromCreateChatMemoReqDto(createChatMemoReqDto);
        chatMemo.associateChat(chat);

        return chatMemoRepository.save(chatMemo);
    }

    @Transactional
    public ChatMemo updateChatMemo(UpdateChatMemoReqDto updateChatMemoReqDto) {
        ChatMemo existingChatMemo = chatMemoRepository.findById(updateChatMemoReqDto.memoId())
                .orElseThrow(() -> new RestApiException(ErrorCode.ENTITY_NOT_FOUND));

        existingChatMemo.updateChatMemoDetails(updateChatMemoReqDto);

        return chatMemoRepository.save(existingChatMemo);
    }

    @Transactional
    public void deleteChatMemo(int chat_id) {

        ChatMemo existingMemo = chatMemoRepository.findById(chat_id)
                .orElseThrow(() -> new RestApiException(ErrorCode.ENTITY_NOT_FOUND));

        chatMemoRepository.delete(existingMemo);
    }

    public ChatMemoDto toChatMemoDto(ChatMemo chatMemo) {
        return chatMemoMapper.toChatMemoDto(chatMemo);
    }
}
