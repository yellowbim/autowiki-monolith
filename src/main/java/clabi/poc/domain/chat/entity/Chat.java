package clabi.poc.domain.chat.entity;

import clabi.poc.domain.chat.dto.chat.req.UpdateChatReqDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "chats")
@Getter
@EntityListeners(AuditingEntityListener.class)
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_id")
    private Integer chatId;

    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    @Column(name = "chat_question", columnDefinition = "TEXT", nullable = false)
    private String chatQuestion;

    @Column(name = "chat_answer", columnDefinition = "TEXT")
    private String chatAnswer;

    @Column(name = "use_token_count")
    private Integer useTokenCount;

    @Column(nullable = false)
    private Float latency;

    @Column(length = 10)
    private String action;

    @Column(name = "sub_action", length = 10)
    private String subAction;

    @Column(name = "recommended_questions", columnDefinition = "json")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<String> recommendedQuestions;

    @Column(name = "`references`", columnDefinition = "json")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<Map<String, Object>> references;

    @Column(name = "images", columnDefinition = "json")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<Map<String, Object>> images;

    @Column(name = "form", columnDefinition = "json")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> form;

    @Column(name = "`table`", columnDefinition = "json")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<Map<String, Object>> table;

    @Column(name = "graph", columnDefinition = "json")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<Map<String, Object>> graph;

    @Column(name = "chat_type", length = 255)
    private String chatType;

    @Column(name = "select_answer")
    private String selectAnswer;

    @Column(name = "chat_history_list", columnDefinition = "json")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<Map<String, Object>> chatHistoryList;

    @Column(name = "select_value", columnDefinition = "json")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<Map<String, Object>> selectValue;

    @Column(name = "example_question_info", columnDefinition = "json")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> exampleQuestionInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_group_id", referencedColumnName = "chat_group_id", nullable = false)
    @JsonIgnore
    private ChatGroup chatGroup;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToOne(mappedBy = "chat", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private ChatSatisfaction chatSatisfaction;

    @OneToOne(mappedBy = "chat", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private ChatMemo chatMemo;

    // 기본 생성자 (JPA 용)
    protected Chat() {
    }

    @Builder
    public Chat(Integer chatId, String ipAddress, String chatQuestion, String chatAnswer, Integer useTokenCount, Float latency, String action, String subAction, List<String> recommendedQuestions, List<Map<String, Object>> references, List<Map<String, Object>> images, List<Map<String, Object>> chatHistoryList, List<Map<String, Object>> selectValue, Map<String, Object> exampleQuestionInfo, ChatGroup chatGroup, ChatSatisfaction chatSatisfaction, ChatMemo chatMemo,
                Map<String, Object> form, String chatType, String selectAnswer, List<Map<String, Object>> table, List<Map<String, Object>> graph) {

        this.chatId = chatId;
        this.ipAddress = ipAddress;
        this.chatQuestion = chatQuestion;
        this.chatAnswer = chatAnswer;
        this.useTokenCount = useTokenCount;
        this.latency = latency;
        this.action = action;
        this.subAction = subAction;
        this.recommendedQuestions = recommendedQuestions;
        this.references = references;
        this.images = images;
        this.chatHistoryList = chatHistoryList;
        this.chatGroup = chatGroup;
        this.chatSatisfaction = chatSatisfaction;
        this.chatMemo = chatMemo;

        this.form = form;
        this.chatType = chatType;
        this.selectAnswer = selectAnswer;
        this.table = table;
        this.graph = graph;
        this.selectValue = selectValue;
        this.exampleQuestionInfo = exampleQuestionInfo;
    }

    public void associateChatGroup(ChatGroup chatGroup) {
        this.chatGroup = chatGroup;
    }

    public void updateChatDetails(UpdateChatReqDto dto) {
        this.ipAddress = dto.ipAddress();
        this.chatQuestion = dto.chatQuestion();
        this.chatAnswer = dto.chatAnswer();
        this.useTokenCount = dto.useTokenCount();
        this.latency = dto.latency();
        this.action = dto.action();
        this.subAction = dto.subAction();
        this.recommendedQuestions = dto.recommendedQuestions() != null ?
                dto.recommendedQuestions() : List.of();

        this.references = dto.references() != null ?
                dto.references() : List.of();

        this.form = dto.form();
        this.chatType = dto.chatType();
        this.selectAnswer = dto.selectAnswer();
        this.table = dto.table() != null ?
                dto.table() : List.of();
        this.graph = dto.graph() != null ?
                dto.graph() : List.of();
        this.images = dto.images() != null ?
                dto.images() : List.of();

        this.chatHistoryList = dto.chatHistoryList() != null ?
                dto.chatHistoryList() : List.of();
        this.selectValue = dto.selectValue() != null ?
                dto.selectValue() : List.of();
        this.exampleQuestionInfo = dto.exampleQuestionInfo();
    }
}
