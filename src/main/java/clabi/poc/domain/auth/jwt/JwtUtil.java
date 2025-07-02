package clabi.poc.domain.auth.jwt;

import clabi.poc.domain.auth.repository.RefreshTokenRepository;
import clabi.poc.domain.common.enums.ErrorCode;
import clabi.poc.domain.member.entity.Member;
import clabi.poc.global.exception.RestApiException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

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
            throw new RestApiException(ErrorCode.JWT_INVALID_ACCESS_TOKEN);
        } catch (ExpiredJwtException e) {
            log.warn("Expired JWT Token : {}", e.getMessage());
            throw new RestApiException(ErrorCode.JWT_ACCESS_TOKEN_EXPIRED);
        } catch (UnsupportedJwtException e) {
            log.warn("Unsupported JWT Token : {}", e.getMessage());
            throw new RestApiException(ErrorCode.JWT_INVALID_ACCESS_TOKEN);
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
                .orElseThrow(() -> new ExpiredJwtException(null, null, ErrorCode.JWT_REFRESH_TOKEN_EXPIRED.getResultMessage()));
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



}
