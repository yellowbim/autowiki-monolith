package clabi.poc.domain.chat.mapper;

import clabi.poc.domain.chat.dto.chat.req.CreateChatReqDto;
import clabi.poc.domain.chat.dto.chat.res.ChatDto;
import clabi.poc.domain.chat.dto.chat.res.ChatWithSatisfactionDto;
import clabi.poc.domain.chat.entity.Chat;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class ChatMapper {

    public abstract Chat toChatEntityFromCreateChatReqDto(CreateChatReqDto createChatGroupReqDto);

    @Mapping(source = "chat.chatGroup.chatGroupId", target = "chatGroupId")
    public abstract ChatDto toChatDto(Chat chat);

    @Mapping(source = "chatGroup.chatGroupId", target = "chatGroupId")
    @Mapping(source = "chatSatisfaction", target = "satisfaction")
    @Mapping(source = "chatMemo", target = "memo")
    public abstract ChatWithSatisfactionDto toChatWithSatisfactionDto(Chat chat);

    public abstract List<ChatWithSatisfactionDto> toChatWithSatisfactionDtoList(List<Chat> chats);
}
