package clabi.poc.domain.chat.entity;

import clabi.poc.domain.chat.dto.chat_group.req.UpdateChatGroupReqDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;


@Entity
@Table(name = "chat_groups")
@Getter
@EntityListeners(AuditingEntityListener.class)
public class ChatGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_group_id")
    private Integer chatGroupId;

    @Column(length = 255, nullable = false)
    private String title;

    @Size(max = 100)
    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "chatGroup", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Chat> chats;

    protected ChatGroup() {
    }

    @Builder
    public ChatGroup(Integer chatGroupId, String title, String createdBy, List<Chat> chats) {
        this.chatGroupId = chatGroupId;
        this.title = title;
        this.createdBy = createdBy;
        this.chats = chats;

        // 연관 엔티티와의 관계 설정
        if (this.chats != null) {
            this.chats.forEach(chat -> chat.associateChatGroup(this));
        }
    }

    public void updateChatGroupDetails(UpdateChatGroupReqDto dto) {
        this.title = dto.title() != null ? dto.title() : this.title;
    }
}
