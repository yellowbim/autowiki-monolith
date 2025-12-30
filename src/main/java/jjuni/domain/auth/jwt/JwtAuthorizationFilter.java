package jjuni.domain.auth.jwt;

import jjuni.domain.auth.service.BlackListService;
import jjuni.domain.common.enums.ErrorCode;
import jjuni.global.exception.JwtAuthenticationException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * [지정한 URL 별 JWT 유효성 검증을 수행하며 직접적인 사용자 '인증'을 확인]
 *
 * @author lee
 * @fileName JwtAuthorizationFilter
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;
    private final BlackListService blackListService;
    private final JwtUtil jwtUtil;

    private static final List<String> EXCLUDE_PATHS = Arrays.asList(
            "/swagger-ui/**",
            "/swagger-resources/**",
            "/swagger.html",
            "/docs/**",
            "/error",
            "/actuator/health",
            "/health",
            "/api/v1/auth/sign-in",
            "/api/v1/auth/reissue-access-token", // refresh token 재발급
            "/favicon.ico", // icon 인데 추가 안하니까 jwt에서 계속 에러 발생
            "/api/v1/chat/group/share/*"
    );

    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    /**
     * JWT 토큰 검증 필터 수행
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain chain)
            throws IOException, ServletException {
        // 요청 url 추출
        String requestURI = request.getRequestURI();

        boolean isExcludedPath = EXCLUDE_PATHS.stream()
                .anyMatch(excludePath -> pathMatcher.match(excludePath, requestURI));

        if (isExcludedPath) {
            chain.doFilter(request, response);
            return;
        }

        // OPTIONS 요청일 경우 => 로직 처리 없이 다음 필터로 이동
        if (request.getMethod().equalsIgnoreCase("OPTIONS")) {
            chain.doFilter(request, response);
            return;
        }

        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new JwtAuthenticationException(ErrorCode.TOKEN_NOT_FOUND);
        }

        String accessToken = authorizationHeader.substring(7);

        if (blackListService.isBlacklisted(accessToken)) {
            throw new JwtAuthenticationException(ErrorCode.TOKEN_BLACKLISTED);
        }

        if (!jwtUtil.validateAccessToken(accessToken)) {
            throw new JwtAuthenticationException(ErrorCode.TOKEN_INVALID);
        }

        String userId = jwtUtil.getUserId(accessToken);
        List<GrantedAuthority> authorities = jwtUtil.getAuthorities(accessToken);

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(userId, null, authorities);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }
}
