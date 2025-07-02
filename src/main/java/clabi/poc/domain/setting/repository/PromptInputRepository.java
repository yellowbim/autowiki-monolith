package clabi.poc.domain.setting.repository;

import clabi.poc.domain.setting.entity.PromptInput;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PromptInputRepository extends JpaRepository<PromptInput, Integer> {
}
