package clabi.poc.domain.setting.service;

import clabi.poc.domain.setting.dto.res.ExampleQuestionCategoryDto;
import clabi.poc.domain.setting.dto.res.ExampleQuestionDto;
import clabi.poc.domain.setting.repository.ExampleQuestionCategoryRepository;
import clabi.poc.domain.setting.repository.ExampleQuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ExampleQuestionService {

    private final ExampleQuestionRepository exampleQuestionRepository;
    private final ExampleQuestionCategoryRepository exampleQuestionCategoryRepository;

    public List<ExampleQuestionCategoryDto> getAllExampleQuestionCategories() {
        return exampleQuestionCategoryRepository.findAll().stream()
                .map(ExampleQuestionCategoryDto::from)
                .collect(Collectors.toList());
    }

    public List<ExampleQuestionDto> getAllExampleQuestions(Integer categoryId) {
        if (categoryId != null) {
            return exampleQuestionRepository.findAllByCategory_Id(categoryId).stream()
                    .map(ExampleQuestionDto::from)
                    .collect(Collectors.toList());
        }
        return exampleQuestionRepository.findFirstQuestionPerCategory().stream()
                .map(ExampleQuestionDto::from)
                .collect(Collectors.toList());
    }
}
