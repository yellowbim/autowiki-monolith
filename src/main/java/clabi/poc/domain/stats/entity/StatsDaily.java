package clabi.poc.domain.stats.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

@Entity
@Table(name = "stats_daily")
@Getter
@EntityListeners(AuditingEntityListener.class)
public class StatsDaily {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "chat_cnt", nullable = false)
    private Integer chatCnt;

    @Column(name = "use_token", nullable = false)
    private Integer useToken;

    @Column(name = "visit_cnt", nullable = false)
    private Integer visitCnt;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDate createdAt;

    public StatsDaily(Integer chatCnt, Integer useToken, Integer visitCnt, LocalDate createdAt) {
        this.chatCnt = chatCnt;
        this.useToken = useToken;
        this.visitCnt = visitCnt;
        this.createdAt = createdAt;
    }

    protected StatsDaily() {}

    public void incrementVisitCount() {
        this.visitCnt += 1;
    }
}
