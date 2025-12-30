package jjuni.global.config;

import org.flywaydb.core.Flyway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("local")
public class FlywayRepairRunner {

    @Bean
    public Flyway flywayBean() {
        Flyway flyway = Flyway.configure()
                .dataSource("jdbc:mysql://localhost:3306/auto-wiki-dev?serverTimezone=Asia/Seoul", "clabi", "Cl@bi789!")
                .locations("classpath:db/migration")
                .baselineOnMigrate(true)     // 기존 DB에도 마이그레이션 허용
                .outOfOrder(true)            // 버전 순서 꼬여도 마이그레이션 허용
                .load();

        flyway.repair();   // 실패한 이력 있으면 제거
        flyway.migrate();  // 실행되지 않은 마이그레이션 파일 수행

        return flyway;
    }
}
