package jjuni.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class LogDirectoryInitializer implements ApplicationListener<ApplicationStartedEvent> {

    @Value("${logging.file.name}")
    private String logFilePath;

    @Value("${spring.profiles.active:}")
    private String activeProfile;

    @Override
    public void onApplicationEvent(@NonNull ApplicationStartedEvent event) {
        if ("local".equalsIgnoreCase(activeProfile)) {
            System.out.println("Local profile detected. Skipping log directory initialization.");
            return;
        }

        File logFile = new File(logFilePath);
        File logDir = logFile.getParentFile();
        if (!logDir.exists()) {
            if (logDir.mkdirs()) {
                System.out.println("로그 디렉토리 생성 완료: " + logDir.getAbsolutePath());
            } else {
                System.err.println("로그 디렉토리 생성 실패: " + logDir.getAbsolutePath());
            }
        }
    }
}
