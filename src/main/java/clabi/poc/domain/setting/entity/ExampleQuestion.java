package clabi.poc.domain.setting.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "example_questions")
@Getter
@EntityListeners(AuditingEntityListener.class)
public class ExampleQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String example;

    // FK + N:1 관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private ExampleQuestionCategory category;

    protected ExampleQuestion() {
    }
}
