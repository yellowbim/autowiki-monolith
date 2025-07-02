package clabi.poc.domain.auth.repository;

import clabi.poc.domain.auth.entity.AccessTokenBlacklist;
import clabi.poc.domain.auth.entity.RefreshTokenEntity;
import jakarta.persistence.Access;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccessTokenBlacklistRepository extends JpaRepository<AccessTokenBlacklist, String> {
    boolean existsByAccessToken(String accessToken);
}
