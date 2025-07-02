package clabi.poc.domain.chat.repository;

import clabi.poc.domain.chat.entity.ChatMemo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMemoRepository extends JpaRepository<ChatMemo, Integer> {
}
