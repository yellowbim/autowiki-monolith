package jjuni.domain.auth.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jjuni.domain.auth.service.BlackListService;
import jjuni.domain.common.enums.ErrorCode;
import jjuni.global.config.security.SecurityExcludePaths;
import jjuni.global.exception.JwtAuthenticationException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
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

    private final AuthenticationEntryPoint authenticationEntryPoint;

    private final BlackListService blackListService;
    private final JwtUtil jwtUtil;

    // 특정 경로만 filter 에서 검증하지 않도록 설정
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();

        return Arrays.stream(SecurityExcludePaths.EXCLUDE_SECURITY_PATHS)
                .anyMatch(pattern ->
                        new AntPathMatcher().match(pattern, path));
    }

    /**
     * JWT 토큰 검증 필터 수행
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain chain)
            throws IOException, ServletException {
        // OPTIONS 요청일 경우 => 로직 처리 없이 다음 필터로 이동
        if (request.getMethod().equalsIgnoreCase("OPTIONS")) {
            chain.doFilter(request, response);
            return;
        }

        try {
            String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                throw new JwtAuthenticationException(ErrorCode.TOKEN_NOT_FOUND);
            }

            String accessToken = authorizationHeader.substring(7);
            request.setAttribute("accessToken", accessToken);

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

            /**
             * context 를 초기화 하는 이유
             * 1. Thread 재사용
             * 2. Filter Chain 예외 흐름
             * 3. async 처리
             * 위 경우 남아있는 경우가 있어서 비정상 작동이 될 수 있음
             */
            SecurityContextHolder.clearContext();
            SecurityContextHolder.getContext().setAuthentication(authentication);
            chain.doFilter(request, response);
        } catch (AuthenticationException ex) {
            SecurityContextHolder.clearContext();
            authenticationEntryPoint.commence(request, response, ex);
        }
    }
}
