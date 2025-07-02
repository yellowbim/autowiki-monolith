package clabi.poc.domain.chat.mapper;

import clabi.poc.domain.chat.dto.chat_memo.res.ChatMemoDto;
import clabi.poc.domain.chat.dto.chat_memo.req.CreateChatMemoReqDto;
import clabi.poc.domain.chat.entity.ChatMemo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class ChatMemoMapper {

    public abstract ChatMemo toChatMemoEntityFromCreateChatMemoReqDto(CreateChatMemoReqDto createChatMemoReqDto);

    @Mapping(source = "chatMemo.chat.chatId", target = "chatId")
    public abstract ChatMemoDto toChatMemoDto(ChatMemo chatMemo);
}
