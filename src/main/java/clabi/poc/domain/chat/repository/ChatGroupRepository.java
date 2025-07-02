package clabi.poc.domain.chat.repository;

import clabi.poc.domain.chat.dto.chat_group.res.ChatGroupGetDto;
import clabi.poc.domain.chat.entity.ChatGroup;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ChatGroupRepository extends JpaRepository<ChatGroup, Integer> {
    List<ChatGroup> findAllByCreatedByAndCreatedAtAfterOrderByCreatedAtDesc(String userId, LocalDateTime after);
}
