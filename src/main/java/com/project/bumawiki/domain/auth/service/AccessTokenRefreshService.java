package com.project.bumawiki.domain.auth.service;

import com.project.bumawiki.domain.auth.domain.RefreshToken;
import com.project.bumawiki.domain.auth.domain.repository.RefreshTokenRepository;
import com.project.bumawiki.global.jwt.dto.TokenResponseDto;
import com.project.bumawiki.global.jwt.exception.RefreshTokenExpiredException;
import com.project.bumawiki.global.jwt.util.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AccessTokenRefreshService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtProvider jwtProvider;

    public TokenResponseDto execute(final String bearerRefreshToken) {
        RefreshToken redisRefreshToken = refreshTokenRepository.findByRefreshToken(bearerRefreshToken)
                .orElseThrow(() -> RefreshTokenExpiredException.EXCEPTION);
        return getNewAccessTokens(redisRefreshToken);
    }

    private TokenResponseDto getNewAccessTokens(final RefreshToken redisRefreshToken) {
        String newAccessToken = jwtProvider.generateAccessToken(redisRefreshToken.getId(), redisRefreshToken.getRole());

        return TokenResponseDto.builder()
                .accessToken(newAccessToken)
                .refreshToken(redisRefreshToken.getRefreshToken())
                .expiredAt(redisRefreshToken.getExpiredAt())
                .build();
    }
}
