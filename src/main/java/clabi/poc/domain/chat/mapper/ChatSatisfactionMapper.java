package clabi.poc.domain.chat.mapper;

import clabi.poc.domain.chat.dto.chat_satisfaction.res.ChatSatisfactionDto;
import clabi.poc.domain.chat.dto.chat_satisfaction.req.CreateChatSatisfactionReqDto;
import clabi.poc.domain.chat.entity.ChatSatisfaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class ChatSatisfactionMapper {

    public abstract ChatSatisfaction toChatSatisfactionEntityFromCreateChatSatisfactionReqDto(CreateChatSatisfactionReqDto createChatSatisfactionReqDto);

    @Mapping(source = "chatSatisfaction.chat.chatId", target = "chatId")
    public abstract ChatSatisfactionDto toChatSatisfactionDto(ChatSatisfaction chatSatisfaction);
}
