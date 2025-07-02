package clabi.poc.domain.member.entity;

import clabi.poc.domain.auth.enums.RoleType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Builder
@Table(name = "user")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})  // JPA에서 lazy관련 에러 날 경우 사용
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // jpa 자동생성
    private Long id;

    @Comment("아이디")
    @Column(columnDefinition = "varchar(50)", nullable = false)
    private String userId;

    @Comment("비밀번호")
    @Column(nullable = false)
    private String password;

    // JWT 사용자 권한 추가
    @Comment("권한")
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(50)", nullable = false)
    private RoleType role;

    @Comment("생성일")
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @Comment("수정일")
    @UpdateTimestamp
    @Column(updatable = false)
    private LocalDateTime updatedAt;
}

