package clabi.poc.domain.setting.repository;

import clabi.poc.domain.setting.dto.res.ExampleQuestionDto;
import clabi.poc.domain.setting.entity.ExampleQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ExampleQuestionRepository extends JpaRepository<ExampleQuestion, Integer> {
    @Query(value = """
    SELECT q.*, c.name AS category_name
    FROM example_questions q
    JOIN example_question_category c ON q.category_id = c.id
    WHERE q.id IN (
        SELECT MIN(id)
        FROM example_questions
        GROUP BY category_id
    ) 
""", nativeQuery = true)
    List<ExampleQuestion> findFirstQuestionPerCategory();

    List<ExampleQuestion> findAllByCategory_Id(Integer categoryId);
}
