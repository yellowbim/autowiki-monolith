package jjuni.domain.auth.repository;

import jjuni.domain.auth.entity.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {
    // Member에 있는 id(seq) 로 조회하는 쿼리
    Optional<RefreshTokenEntity> findByMember_Id(Long userId);

    void deleteByMember_Id(Long memberId);
}
