package clabi.poc.domain.chat.entity;

import clabi.poc.domain.chat.dto.chat_satisfaction.req.UpdateChatSatisfactionReqDto;
import clabi.poc.domain.chat.enums.SatisfactionType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.List;


@Entity
@Table(name = "chat_satisfactions")
@Getter
@EntityListeners(AuditingEntityListener.class)
public class ChatSatisfaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "satisfaction_id")
    private Integer satisfactionId;


    @Column(name = "satisfaction_content", length = 255, nullable = true)
    private String satisfactionContent;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_id", referencedColumnName = "chat_id", nullable = false)
    @JsonIgnore
    private Chat chat;

    @Enumerated(EnumType.STRING)
    @Column(name = "satisfaction_type", nullable = false)
    private SatisfactionType satisfactionType;

    protected ChatSatisfaction() {
    }

    @Builder
    public ChatSatisfaction(Integer satisfactionId, String satisfactionContent, Chat chat, SatisfactionType satisfactionType) {
        this.satisfactionId = satisfactionId;
        this.satisfactionContent = satisfactionContent;
        this.satisfactionType = satisfactionType;
        this.chat = chat;
    }

    public void associateChat(Chat chat) {
        this.chat = chat;
    }

    public void updateChatSatisfactionDetails(UpdateChatSatisfactionReqDto dto) {
        this.satisfactionContent = dto.satisfactionContent() != null ? dto.satisfactionContent() : this.satisfactionContent;
        this.satisfactionType = dto.satisfactionType() != null ? dto.satisfactionType() : this.satisfactionType;
    }
}
