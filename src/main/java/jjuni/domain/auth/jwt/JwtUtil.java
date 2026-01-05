package jjuni.domain.auth.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import jjuni.domain.auth.repository.RefreshTokenRepository;
import jjuni.domain.common.enums.ErrorCode;
import jjuni.domain.member.entity.Member;
import jjuni.global.exception.RestApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * [JWT 관련 메서드를 제공하는 클래스]
 */
@Slf4j
@Component
public class JwtUtil {
    private final Key key;
    private final long accessTokenExpTime;
    private final long refreshTokenExpTime;
    private final RefreshTokenRepository refreshTokenRepository;

    public JwtUtil(
            @Value("${jwt.secret}") String secretKey,
            @Value("${jwt.access-expiration-time}") long accessTokenExpTime,
            @Value("${jwt.refresh-expiration-time}") long refreshTokenExpTime,
            RefreshTokenRepository refreshTokenRepository
    ) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.accessTokenExpTime = accessTokenExpTime;
        this.refreshTokenExpTime = refreshTokenExpTime;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    ////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////// Access Token ////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////
    /**
     * Access Token 생성
     * @param member
     * @return Access Token String
     */
    public String createAccessToken(Member member) {
        Claims claims = Jwts.claims();
        claims.put("id", member.getId()); // 내부적으로 처리되는 ID (Integer)
        claims.put("userId", member.getUserId()); // 사용자가 사용하는 userId (String)
        claims.put("role", member.getRole());

        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime tokenValidity = now.plusSeconds(accessTokenExpTime);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(Date.from(now.toInstant()))
                .setExpiration(Date.from(tokenValidity.toInstant()))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Access Token 재발급
     * - 이전 Token에서 Calim 정보 추출 후 다시 셋팅
     * @param accessToken
     * @return Access Token String
     */
    public String reIssueAccessToken(String accessToken) {
        // Access Tokne에서 Claim 추출
        Claims parseClaims = parseClaims(accessToken);
        Claims claims = Jwts.claims();
        claims.put("id", parseClaims.get("id", Long.class)); // 내부적으로 처리되는 ID (Integer)
        claims.put("userId", parseClaims.get("userId", String.class)); // 사용자가 사용하는 userId (String)
        claims.put("role", parseClaims.get("role", String.class));

        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime tokenValidity = now.plusSeconds(accessTokenExpTime);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(Date.from(now.toInstant()))
                .setExpiration(Date.from(tokenValidity.toInstant()))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    ////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////// Access Token ////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////
    /**
     * Refresh Token 생성
     * - refresh token 은 Access Token 을 초기화 시키는 용도이기때문에 사용자정보를 굳이 담을 필요가 없음
     * @return Refresh Token String
     */
    public String createRefreshToken() {
        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime tokenValidity = now.plusSeconds(refreshTokenExpTime);
        return Jwts.builder()
                .setIssuedAt(Date.from(now.toInstant()))
                .setExpiration(Date.from(tokenValidity.toInstant()))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    ////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////// LOG OUT ////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////


    /**
     * Token에서 ID 추출
     * @param token
     * @return ID (Long)
     */
    public Long getId(String token) {
        return parseClaims(token).get("id", Long.class);
    }

    /**
     * Token에서 User ID 추출
     * @param token
     * @return User ID (String)
     */
    public String getUserId(String token) {
        Claims claims = parseClaims(token);
        log.debug("Parsed claims: {}", claims);

        Object userId = claims.get("userId");
        if (userId == null) {
            throw new JwtException("userId 클레임이 없습니다.");
        }
        if (!(userId instanceof String)) {
            log.warn("userId 클레임 타입이 String이 아닙니다: {}", userId.getClass());
            return userId.toString(); // 혹은 throw
        }
        return (String) userId;
//        return parseClaims(token).get("userId", String.class);
    }

    /**
     * Access Token 추출
     * @return
     */
//    public String resolveCurrentAccessToken() {
//        HttpServletRequest request =
//                ((ServletRequestAttributes) RequestContextHolder
//                        .currentRequestAttributes())
//                        .getRequest();
//
//        return request.getHeader(HttpHeaders.AUTHORIZATION).substring(7);
//    }


    /**
     * Access Token 검증
     * @param accessToken
     * @return IsValidate
     */
    public boolean validateAccessToken(String accessToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.warn("Invalid JWT Token : {}", e.getMessage());
            throw new RestApiException(ErrorCode.TOKEN_INVALID);
        } catch (ExpiredJwtException e) {
            log.warn("Expired JWT Token : {}", e.getMessage());
            throw new RestApiException(ErrorCode.ACCESS_TOKEN_EXPIRED);
        } catch (UnsupportedJwtException e) {
            log.warn("Unsupported JWT Token : {}", e.getMessage());
            throw new RestApiException(ErrorCode.TOKEN_INVALID);
        } catch (IllegalArgumentException e) {
            log.warn("JWT claims string is empty : {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Refresh Token 검증
     * - 토큰 자체 유효성 확인
     * - DB에 존재하는 Token 인지 확인
     * - Refresh Token 만료 여부 확인
     *
     * @param refreshToken
     * @return IsValidate
     */
    @Transactional(readOnly = true)
    public void validateRefreshToken(String refreshToken, String expiredAccessToken) throws ExpiredJwtException {
        Long userId = 0L;
        try {
            // Refresh Token 유효성 검증
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(refreshToken);

            // Access Token 에서 userId 조회
            userId = getId(expiredAccessToken);
        } catch (ExpiredJwtException e) {
            log.warn("Refresh Token Expired : {}", e.getMessage());
            throw new ExpiredJwtException(null, null, "Refresh Token Expired");
        } catch (Exception e) {
            log.warn("validate RefreshToken Token Fail : {}", e.getMessage());
            throw e;
        }

        // DB 에서 정보 조회
        refreshTokenRepository.findByMember_Id(userId)
                .filter(userRefreshToken -> userRefreshToken.getRefreshToken().equals(refreshToken))
                .orElseThrow(() -> new ExpiredJwtException(null, null, ErrorCode.REFRESH_TOKEN_EXPIRED.getMessageKey()));
    }


    /**
     * JWT Claims 추출
     * @param accessToken
     * @return JWT Claims
     */
    public Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }


    /**
     * JWT 만료시간 추출
     * @param token
     * @return
     */
    public LocalDateTime getExpiration(String token) {
        Date expiration = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();

        return expiration.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    /**
     * 권한 추출
     * @param accessToken
     * @return
     */
    public List<GrantedAuthority> getAuthorities(String accessToken) {
        Claims claims = parseClaims(accessToken);

        Object roles = claims.get("roles");
        if (roles == null) {
            return Collections.emptyList();
        }

        @SuppressWarnings("unchecked")
        List<String> roleList = (List<String>) roles;

        return roleList.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
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
        validateRefreshToken(refreshToken, expiredAccessToken);
        // Access Token 재발급
        return reIssueAccessToken(expiredAccessToken);
    }


}
