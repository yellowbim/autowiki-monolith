package clabi.poc.domain.chat.repository;

import clabi.poc.domain.chat.entity.ChatSatisfaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatSatisfactionRepository extends JpaRepository<ChatSatisfaction, Integer> {
}
