package jjuni.global.config;


import jjuni.domain.auth.jwt.JwtAuthorizationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    // swagger, 로그인 예외 경로 설정
    private static final String[] excludePath = {
            "/",
            "/swagger-ui/**",
            "/swagger-resources/**",
            "/swagger.html",
            "/docs/**",
            "/actuator/health",
            "/health",
            "/api/v1/auth/sign-in",
            "/error",
            "/api/v1/auth/reissue-access-token", // 재발급 요청 API
            "/api/v1/chat/group/share/*"

    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, JwtAuthorizationFilter jwtAuthorizationFilter)
            throws Exception {

        httpSecurity
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .httpBasic(http -> http.disable())
                .formLogin(form -> form.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthorizationFilter, BasicAuthenticationFilter.class) // jwt 필터 추가
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(excludePath).permitAll()
                        .anyRequest().authenticated());

        return httpSecurity.getOrBuild();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers(excludePath);
    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type", "x-auth-token"));
        configuration.setExposedHeaders(List.of("x-auth-token"));
//        configuration.setMaxAge(3600L); // 사전 요청 결과를 3600초(1시간) 동안 캐시

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
