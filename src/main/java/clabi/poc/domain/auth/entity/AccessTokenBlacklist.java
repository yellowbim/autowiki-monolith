package clabi.poc.domain.auth.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "access_token_blacklist")
public class AccessTokenBlacklist {

    @Id
    @Column(length = 512, nullable = false, unique = true)
    private String accessToken;

    @Column(nullable = false)
    private LocalDateTime expiredAt;

    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public AccessTokenBlacklist(String accessToken, LocalDateTime expiredAt) {
        this.accessToken = accessToken;
        this.expiredAt = expiredAt;
    }
}
