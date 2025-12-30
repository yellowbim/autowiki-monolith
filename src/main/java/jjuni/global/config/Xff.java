package jjuni.global.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

@Component
public class Xff {

    public String getClientIp(HttpServletRequest request) {
        String xffHeader = request.getHeader("X-Forwarded-For");
        if (xffHeader != null && !xffHeader.isEmpty()) {
            return xffHeader.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
