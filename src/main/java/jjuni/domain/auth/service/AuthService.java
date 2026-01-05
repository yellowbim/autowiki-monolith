package jjuni.domain.auth.service;


import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import jjuni.domain.auth.dto.*;
import jjuni.domain.auth.entity.AccessTokenBlacklist;
import jjuni.domain.auth.entity.RefreshTokenEntity;
import jjuni.domain.auth.jwt.JwtAuthorizationFilter;
import jjuni.domain.auth.jwt.JwtUtil;
import jjuni.domain.auth.repository.AccessTokenBlacklistRepository;
import jjuni.domain.auth.repository.RefreshTokenRepository;
import jjuni.domain.common.enums.ErrorCode;
import jjuni.domain.member.entity.Member;
import jjuni.domain.member.repository.MemberRepository;
import jjuni.global.exception.RestApiException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final AccessTokenBlacklistRepository accessTokenBlacklistRepository;
    private final PasswordEncoder encoder = new BCryptPasswordEncoder(); // 단방향 해시 암호화
    private final JwtUtil jwtUtil;
    private final JwtAuthorizationFilter jwtAuthorizationFilter;

    /**
     * 사용자 회원가입
     *
     * @param req
     */
    @Transactional
    public SignUpResponse signUp(SignUpRequest req) throws Exception {
        // id로 사용자 정보 조회
        Optional<Member> findMember = memberRepository.findByUserId(req.userId());
        if (findMember.isPresent()) {
            throw new RestApiException(ErrorCode.USER_ALREADY_EXISTS);
        }

        Member newMember = req.toEntity();
        try {
            newMember.setPassword(encoder.encode(req.password()));
        } catch (Exception e) {
            throw new Exception(e);
        }

        // 사용자 등록
        try {
            memberRepository.save(newMember);
        } catch (Exception e) {
            throw new Exception(e);
        }
        return SignUpResponse.toDto(newMember);
    }


    /**
     * 사용자 로그인
     * - jwt refresh token이 만료되었을때 에러처리하지 않고 update를 진행하기 위함
     * - 로그인 시 Access Token 만 발급
     * - 로그아웃 시 Refresh Token 재발급
     * 
     * @param req
     */
    @Transactional
    public SignInResponse signIn(SignInRequest req) {
        // user id로 사용자 정보 조회
        Optional<Member> memberInfo = memberRepository.findByUserId(req.userId());
        if (!memberInfo.isPresent()) { // 사용자가 존재하지 않습니다.
            throw new RestApiException(ErrorCode.USER_NOT_FOUND);
        }
        Member member = memberInfo.get();

        if (!encoder.matches(req.password(), member.getPassword())) {
            throw new RestApiException(ErrorCode.INVALID_ARGUMENT);
        }

        // Access Token 발급
        String accessToken = jwtUtil.createAccessToken(memberInfo.get());

        // Refresh Token 발급 및 DB에 만료된게 있으면 update
        String refreshToken = jwtUtil.createRefreshToken();
        Optional<RefreshTokenEntity> existing = refreshTokenRepository.findByMember_Id(member.getId());

        RefreshTokenEntity entity = existing
                .map(r -> {
                    r.setRefreshToken(refreshToken);         // 새로운 토큰으로 갱신
                    r.setUpdatedAt(LocalDateTime.now());
                    return r;
                })
                .orElse(new RefreshTokenEntity(member, refreshToken));

        refreshTokenRepository.save(entity);

        // access token 발급
        return new SignInResponse(memberInfo.get().getRole(), accessToken, refreshToken);
    }

    /**
     * 사용자 로그아웃
     * - 회원 refresh token 삭제
     */
    @Transactional
    public void signOut(HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userId = (String) auth.getPrincipal();

        // id로 사용자 정보 조회
        Optional<Member> memberInfo = memberRepository.findByUserId(userId);
        if (!memberInfo.isPresent()) { // 사용자가 존재하지 않습니다.
            throw new RestApiException(ErrorCode.USER_NOT_FOUND);
        }
        Member member = memberInfo.get();

        // 회원 refreshtoken 삭제
        refreshTokenRepository.deleteByMember_Id((member.getId()));

        // Access Token 추출
        String accessToken = (String) request.getAttribute("accessToken");

        // Access Token 만료 시각 파싱
        LocalDateTime expiredAt = jwtUtil.getExpiration(accessToken);

        // access token black list 등록
        AccessTokenBlacklist blacklisted = new AccessTokenBlacklist(accessToken, expiredAt);
        accessTokenBlacklistRepository.save(blacklisted);
    }


    /**
     * Access Token 재발급
     *
     * @param req
     */
    @Transactional
    public RefreshTokenResponse reIssueAccessToken(HttpServletRequest request, String refreshToken) throws Exception {
        // id로 사용자 정보 조회
        if (refreshToken == null) {
            throw new RestApiException(ErrorCode.TOKEN_INVALID);
        }

        String accessToken = jwtUtil.reIssueAccessToken(request, refreshToken);

        // access token 발급
        return RefreshTokenResponse.builder()
                .accessToken(accessToken)
                .build();
    }

    /**
     * 로그인 시 refresh token이 만료되었는지 판단하여 return 해주는 함수
     * - 만료 전 : 기존 DB 정보
     * - 만료 후 : 신규 발급 및 DB 저장
     * @param tokenEntity
     * @param accessToken
     * @return
     */
    public String validateAndRefreshToken(RefreshTokenEntity tokenEntity, String accessToken) {
        try {
            jwtUtil.validateRefreshToken(tokenEntity.getRefreshToken(), accessToken);
            log.info("Refresh Token 만료 전");
            return tokenEntity.getRefreshToken();
        } catch (ExpiredJwtException e) {
            log.info("Refresh Token 만료로 인한 재발급");
            String newRefreshToken = jwtUtil.createRefreshToken();
            tokenEntity.updateRefreshToken(newRefreshToken);
            refreshTokenRepository.save(tokenEntity);
            return newRefreshToken;
        }
    }

}