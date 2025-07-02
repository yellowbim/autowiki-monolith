package clabi.poc.domain.setting.repository;

import clabi.poc.domain.setting.entity.Greeting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GreetingRepository extends JpaRepository<Greeting, Integer> {
}
