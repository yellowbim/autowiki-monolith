package clabi.poc.domain.chat.entity;

import clabi.poc.domain.chat.dto.chat_memo.req.UpdateChatMemoReqDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.List;


@Entity
@Table(name = "chat_memos")
@Getter
@EntityListeners(AuditingEntityListener.class)
public class ChatMemo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "memo_id")
    private Integer memoId;

    @Column(name = "memo_content", columnDefinition = "TEXT")
    private String memoContent;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_id", referencedColumnName = "chat_id", nullable = false)
    @JsonIgnore
    private Chat chat;

    protected ChatMemo() {
    }

    @Builder
    public ChatMemo(Integer memoId, String memoContent, Chat chat) {
        this.memoId = memoId;
        this.memoContent = memoContent;
        this.chat = chat;
    }

    public void associateChat(Chat chat) {
        this.chat = chat;
    }

    public void updateChatMemoDetails(UpdateChatMemoReqDto dto) {
        this.memoContent = dto.memoContent() != null ? dto.memoContent() : this.memoContent;
    }
}
