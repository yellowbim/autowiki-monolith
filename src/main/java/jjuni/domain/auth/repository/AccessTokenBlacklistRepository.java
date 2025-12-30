package jjuni.domain.auth.repository;

import jjuni.domain.auth.entity.AccessTokenBlacklist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccessTokenBlacklistRepository extends JpaRepository<AccessTokenBlacklist, String> {
    boolean existsByAccessToken(String accessToken);
}
