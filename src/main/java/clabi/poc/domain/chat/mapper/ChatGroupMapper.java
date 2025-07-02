package clabi.poc.domain.chat.mapper;

import clabi.poc.domain.chat.dto.chat.res.ChatDto;
import clabi.poc.domain.chat.dto.chat_group.res.ChatGroupDto;
import clabi.poc.domain.chat.dto.chat_group.req.CreateChatGroupReqDto;
import clabi.poc.domain.chat.entity.Chat;
import clabi.poc.domain.chat.entity.ChatGroup;
import org.mapstruct.*;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {ChatMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class ChatGroupMapper {

    public abstract ChatGroup toChatGroupEntity(CreateChatGroupReqDto createChatGroupDto);

    @IterableMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
    @Mapping(source = "chats", target = "chats")
    public abstract ChatGroupDto toChatGroupDto(ChatGroup chatGroup);
}
