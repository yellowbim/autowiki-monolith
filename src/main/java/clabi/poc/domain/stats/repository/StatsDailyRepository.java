package clabi.poc.domain.stats.repository;

import clabi.poc.domain.stats.entity.StatsDaily;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

public interface StatsDailyRepository extends JpaRepository<StatsDaily, Integer> {

    @Modifying
    @Transactional
    @Query("UPDATE StatsDaily s SET s.chatCnt = s.chatCnt + 1 WHERE s.createdAt = :today")
    void incrementChatCnt(@Param("today") LocalDate today);

    @Modifying
    @Transactional
    @Query("UPDATE StatsDaily s SET s.useToken = s.useToken + :useToken WHERE s.createdAt = :today")
    void incrementUseToken(@Param("useToken") int useToken, @Param("today") LocalDate today);

    Optional<StatsDaily> findByCreatedAt(LocalDate createdAt);
}
