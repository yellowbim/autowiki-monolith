package clabi.poc.domain.setting.repository;

import clabi.poc.domain.setting.dto.res.ExampleQuestionDto;
import clabi.poc.domain.setting.entity.ExampleQuestion;
import clabi.poc.domain.setting.entity.ExampleQuestionCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ExampleQuestionCategoryRepository extends JpaRepository<ExampleQuestionCategory, Integer> {
}
