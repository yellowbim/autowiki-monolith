package jjuni.global.config.security;

public final class SecurityExcludePaths {
    public static final String[] EXCLUDE_SECURITY_PATHS = {
            "/v3/**",
            "/",
            "/swagger-ui/**",
            "/swagger-resources/**",
            "/swagger.html",
            "/docs/**",
            "/error",
            "/actuator/health",
            "/health",
            "/favicon.ico",
            "/api/v1/auth/sign-up",
            "/api/v1/auth/sign-in"
    };

    private SecurityExcludePaths() {}
}
