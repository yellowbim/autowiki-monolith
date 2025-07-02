package clabi.poc.domain.auth.jwt;

import clabi.poc.domain.auth.service.BlackListService;
import clabi.poc.domain.common.dto.ErrorResponse;
import clabi.poc.domain.common.enums.ErrorCode;
import clabi.poc.global.exception.RestApiException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
        try {
            // Header 내에 토큰이 존재하는 경우
            if (authorizationHeader != null && !authorizationHeader.equalsIgnoreCase("")) {
                // Header 내에 토큰을 추출
                String accessToken = authorizationHeader.substring(7);
                // 블랙리스트 검증
                if (blackListService.isBlacklisted(accessToken)) {
                    throw new RestApiException(ErrorCode.EXIST_BLACK_LIST);
                }
                // 추출한 토큰이 유효한지 여부를 체크
                if (jwtUtil.validateAccessToken(accessToken)) {
                    // 토큰을 기반으로 사용자 아이디를 반환 받는 메서드
                    String userId = jwtUtil.getUserId(accessToken);
                    logger.debug("[+] user id Check: " + userId);
                    // 사용자 아이디가 존재하는지 여부 체크
                    if (userId != null) {
                        // 사용자 정보 조회 후 security context 등록 (userId로 체크) - 여기서 기본으로 String interface를 받기 때문에 string 처리해서 전달
                        UserDetails userDetails = userDetailsService.loadUserByUsername(userId);
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        chain.doFilter(request, response);
                    } else {
                        throw new RestApiException(ErrorCode.NOT_FOUND_USER);
                    }
                } else {
                    throw new RestApiException(ErrorCode.JWT_INVALID_ACCESS_TOKEN);
                }
            } else {
                throw new RestApiException(ErrorCode.JWT_NOT_FIND_TOKEN);
            }
        } catch (RestApiException e) {
            // JWT 관련 커스텀 예외만 직접 처리
            ErrorCode errorCode = extractErrorCode(e);
            setJwtErrorResponse(response, errorCode);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Access Token 만료로 재발급 요청 시 Access Token 재발급
     *
     * @param request
     */
    public String reIssueAccessToken(HttpServletRequest request, String refreshToken) {
        // 만료된 Access Token 추출
        String expiredAccessToken = request.getHeader(HttpHeaders.AUTHORIZATION).substring(7);
        // Refresh Token 검증(사용자 정보, DB 존재 여부, 만료 여부)
        jwtUtil.validateRefreshToken(refreshToken, expiredAccessToken);
        // Access Token 재발급
        String newAccessToken = jwtUtil.reIssueAccessToken(expiredAccessToken);
        // 사용자 정보 조회 후 security context 등록 (userId로 체크)
        UserDetails userDetails = userDetailsService.loadUserByUsername(jwtUtil.getUserId(newAccessToken));
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return newAccessToken;
    }

    /**
     * Access Token 만료로 요청 시 Access Token 재발급
     * - 미사용
     * @param request
     * @param response
     * @param exception
     */
    private void reIssueAccessToken(HttpServletRequest request, HttpServletResponse response, Exception exception) {
        try {
            // 만료된 Access Token 확인
            String expiredAccessToken = request.getHeader(HttpHeaders.AUTHORIZATION).substring(7);
            String refreshToken = request.getHeader("Refresh-Token");
            // Refresh Token 검증(사용자 정보, DB 존재 여부, 만료 여부)
            jwtUtil.validateRefreshToken(refreshToken, expiredAccessToken);
            // Access Toen 재발급
            String newAccessToken = jwtUtil.reIssueAccessToken(expiredAccessToken);
            // 사용자 정보 조회 후 security context 등록 (userId로 체크)
            UserDetails userDetails = userDetailsService.loadUserByUsername(jwtUtil.getUserId(newAccessToken));
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            response.setHeader("New-Access-Token", newAccessToken);
        } catch (Exception e) {
            request.setAttribute("exception", e);
        }
    }

    /**
     * 토큰 관련 Exception 발생 시 에러코드 매핑
     *
     * @param e Exception
     * @return JSONObject
     */
    private ErrorCode extractErrorCode(Exception e) {
        if (e instanceof RestApiException) {
            return ((RestApiException) e).getErrorCode();
        }
        return ErrorCode.UNAUTHORIZED;
    }


    private void setJwtErrorResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {
        response.setStatus(errorCode.getResultHttpStatus().value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ErrorResponse error = ErrorResponse.from(errorCode);
        String json = new ObjectMapper().writeValueAsString(error);

        response.getWriter().write(json);
    }

}
