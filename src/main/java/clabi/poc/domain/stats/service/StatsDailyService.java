package clabi.poc.domain.stats.service;

import clabi.poc.domain.common.enums.ErrorCode;
import clabi.poc.domain.stats.entity.StatsDaily;
import clabi.poc.domain.stats.repository.StatsDailyRepository;
import clabi.poc.global.exception.GlobalExceptionHandler;
import clabi.poc.global.exception.RestApiException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Transactional(readOnly = true)
public class StatsDailyService {
    private final StatsDailyRepository statsDailyRepository;

    public StatsDailyService(StatsDailyRepository statsDailyRepository) {
        this.statsDailyRepository = statsDailyRepository;
    }

    @Transactional
    public void updateStatsDaily(Integer useToken) {
        LocalDate today = LocalDate.now();

        try {
            statsDailyRepository.findByCreatedAt(today)
                    .orElseThrow(() -> new RestApiException(ErrorCode.STATS_NOT_FOUND)); //오늘 날짜에 해당하는 StatsDaily 레코드를 찾지 못했을 때 발생

            updateStatsDailyCounters(today, useToken); // 오늘 날짜에 해당하는 StatsDaily 레코드를 조회 후, 업데이트

        } catch (RestApiException e) {
            try {
                createNewStatsDaily(today, useToken); // 동시성 문제 발생 시, StatsDaily 레코드 생성

            } catch (DataIntegrityViolationException ex) { //데이터베이스에서 데이터 무결성 제약 조건이 위반되었을 때 발생하는 예외
                updateStatsDailyCounters(today, useToken);
            }
        } catch (Exception e) {
            throw new RestApiException(ErrorCode.INTERNAL_ERROR);
        }
    }

    private void updateStatsDailyCounters(LocalDate today, Integer useToken) {
        statsDailyRepository.incrementChatCnt(today);
        statsDailyRepository.incrementUseToken(useToken, today);
    }

    private void createNewStatsDaily(LocalDate today, Integer useToken) {
        StatsDaily newStatsDaily = new StatsDaily(0, 0, 0, today);
        statsDailyRepository.save(newStatsDaily);

        updateStatsDailyCounters(today, useToken);
    }
}
