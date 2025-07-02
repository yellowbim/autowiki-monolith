package clabi.poc.domain.setting.service;

import clabi.poc.domain.setting.entity.PromptInput;
import clabi.poc.domain.setting.repository.PromptInputRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class PromptInputService {
    private final PromptInputRepository promptInputRepository;

    public PromptInputService(PromptInputRepository promptInputRepository) {
        this.promptInputRepository = promptInputRepository;
    }

    public List<PromptInput> getPromptInput() {
        return promptInputRepository.findAll().stream().findFirst()
                .map(Collections::singletonList)
                .orElse(Collections.emptyList());
    }
}
