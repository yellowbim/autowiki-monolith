package clabi.poc.global.config;

import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
@Aspect
@Component
public class LoggingAspect {
    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void controller() {}

    @Around("controller()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            logger.warn("ServletRequestAttributes is null, cannot log request information.");
            return joinPoint.proceed();
        }
        HttpServletRequest request = attributes.getRequest();

        String url = request.getRequestURL().toString();
        String queryString = request.getQueryString();
        if (queryString != null) {
            url += "?" + queryString;
        }

        if (url.contains("/docs") || url.contains("/swagger") || url.contains("/v3/api-docs")) {
            return joinPoint.proceed();
        }

        logger.info("API 호출 URL: {}", url);

        String method = request.getMethod();
        if (!"GET".equalsIgnoreCase(method) && !"DELETE".equalsIgnoreCase(method)) {
            logRequestBody(joinPoint);
        }

        Object result = joinPoint.proceed();

        logger.info("리턴 값: {}", result);
        return result;
    }

    private void logRequestBody(ProceedingJoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg instanceof MultipartFile file) {
                logger.info("Request Body: name={}, size={}", file.getOriginalFilename(), file.getSize());
            } else {
                logger.info("Request Body: {}", arg);
            }
        }
    }
}
