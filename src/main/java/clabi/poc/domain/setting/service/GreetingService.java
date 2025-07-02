package clabi.poc.domain.setting.service;

import clabi.poc.domain.setting.entity.Greeting;
import clabi.poc.domain.setting.repository.GreetingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;


@Service
@Transactional(readOnly = true)
public class GreetingService {
    private final GreetingRepository greetingRepository;

    public GreetingService(GreetingRepository greetingRepository) {
        this.greetingRepository = greetingRepository;
    }

    public List<Greeting> getGreeting() {
        return greetingRepository.findAll().stream().findFirst()
                .map(Collections::singletonList)
                .orElse(Collections.emptyList());
    }
}
