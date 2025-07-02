package clabi.poc.domain.setting.entity;


import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@Table(name = "prompt_input")
@EntityListeners(AuditingEntityListener.class)
public class PromptInput {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "input", columnDefinition = "TEXT", nullable = false)
    private String input;
}
