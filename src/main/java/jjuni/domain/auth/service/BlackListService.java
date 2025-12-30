package jjuni.domain.auth.service;

import jjuni.domain.auth.repository.AccessTokenBlacklistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BlackListService {

    private final AccessTokenBlacklistRepository accessTokenBlacklistRepository;

    /**
     * access token blacklist 존재 여부 확인
     * @param token
     * @return
     */
    public boolean isBlacklisted(String token) {
        return accessTokenBlacklistRepository.existsByAccessToken(token);
    }

}
