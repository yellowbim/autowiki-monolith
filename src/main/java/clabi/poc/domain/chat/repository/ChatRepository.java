package clabi.poc.domain.chat.repository;

import clabi.poc.domain.chat.entity.Chat;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, Integer> {
    @EntityGraph(attributePaths = {"chatSatisfaction", "chatMemo"}) // chat 에 있는 만족도, chatting 모든 정보를 추출하기 위함
    List<Chat> findAllByChatGroup_ChatGroupIdOrderByChatIdAsc(Integer chatGroup_chatGroupId);
}
