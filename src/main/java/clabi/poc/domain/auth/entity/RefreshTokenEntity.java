package clabi.poc.domain.auth.entity;

import clabi.poc.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Comment("사용자별 Refresh Token 테이블")
@Table(name = "refresh_token")
public class RefreshTokenEntity {

    @Id
    @Comment("Refresh token seq")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Comment("아이디")
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", unique = true)
    private Member member;

    @Comment("리프레시 토큰 정보")
    // refresh token의 길이가 100을 넘어서 우선 제외
//    @Column(columnDefinition = "varchar(100)", nullable = false)
    @Column(nullable = false)
    private String refreshToken;

//    @Comment("리프레시 토큰 만료일")
//    @Column(columnDefinition = "varchar(100)", nullable = false)
//    private String refreshTokenExpireDate;

    @Comment("생성일")
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @Comment("수정일")
    @UpdateTimestamp
    @Column
    private LocalDateTime updatedAt;

    public RefreshTokenEntity(Member member, String refreshToken) {
        this.member = member;
        this.refreshToken = refreshToken;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        this.updatedAt = LocalDateTime.now();
    }
}
