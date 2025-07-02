package clabi.poc.global.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

@Slf4j
@Profile({"local-dev", "local-test"})
@Configuration
@RequiredArgsConstructor
public class SshDataSourceConfig {

    private final SshTunnelInitializer ssh;

    @Bean
    @Primary
    public DataSource dataSource(DataSourceProperties properties) {
        // 포트포워딩 먼저 수행
        int forwardedPort = ssh.buildSshConnection();
        String finalUrl = properties.getUrl().replace("[forwardedPort]", String.valueOf(forwardedPort));
        log.info("최종 DB URL: {}", finalUrl);

        return DataSourceBuilder.create()
                .url(finalUrl)
                .username(properties.getUsername())
                .password(properties.getPassword())
                .build();
    }
}
