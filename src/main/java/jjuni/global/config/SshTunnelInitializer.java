package jjuni.global.config;

import jakarta.annotation.PreDestroy;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Properties;

@Slf4j
@Profile({"local-dev", "local-test"})
@ConfigurationProperties(prefix = "ssh")
@Component
@Validated
@Setter
public class SshTunnelInitializer {
    @NotNull private BastionConfig bastion;
    @NotNull private RemoteConfig remote;

    private Session session;

    @Getter @Setter
    public static class BastionConfig {
        @NotNull private String host;
        @NotNull private String user;
        @NotNull private String password;
    }

    @Getter @Setter
    public static class RemoteConfig {
        @NotNull private String host;
        @NotNull private int port;
    }

    @PreDestroy
    public void closeSSH() {
        if (session != null && session.isConnected()) {
            session.disconnect();
            log.info("SSH 연결 종료");
        }
    }

    public Integer buildSshConnection() {
        Integer forwardedPort = null;

        try {
            log.info("SSH 연결 시작: {}@{}: {}",
                    bastion.getUser(), bastion.getHost(), 22);

            JSch jsch = new JSch();
            session = jsch.getSession(bastion.getUser(), bastion.getHost(), 22);
            session.setPassword(bastion.getPassword());

            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);

            session.connect();
            log.info("SSH 연결 성공");

            forwardedPort = session.setPortForwardingL(
                    0, remote.getHost(), remote.getPort()
            );
            log.info("포트포워딩 성공: localhost:{} -> {}: {}",
                    forwardedPort, remote.getHost(), remote.getPort());
        } catch (Exception e) {
            log.error("SSH 터널 연결 실패", e);
            closeSSH();
            System.exit(1);
        }

        return forwardedPort;
    }
}
