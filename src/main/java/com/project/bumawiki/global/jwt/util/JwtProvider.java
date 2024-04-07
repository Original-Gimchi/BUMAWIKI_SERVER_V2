package com.project.bumawiki.global.jwt.util;

import static com.project.bumawiki.global.jwt.properties.JwtConstants.*;

import java.time.ZonedDateTime;
import java.util.Date;

import org.springframework.stereotype.Component;

import com.project.bumawiki.domain.auth.domain.RefreshToken;
import com.project.bumawiki.domain.auth.domain.repository.RefreshTokenRepository;
import com.project.bumawiki.global.jwt.dto.TokenResponseDto;
import com.project.bumawiki.global.jwt.properties.JwtProperties;

import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtProvider {
	private final JwtProperties jwtProperties;
	private final RefreshTokenRepository refreshTokenRepository;

	public String generateAccessToken(String authId, String role) {
		return jwtProperties.getPrefix() + EMPTY.getMessage() + generateToken(authId, role, ACCESS_KEY.getMessage(),
			jwtProperties.getAccessExp());
	}

	public TokenResponseDto generateToken(String authId, String role) {
		String accessToken =
			jwtProperties.getPrefix() + EMPTY.getMessage() + generateToken(authId, role, ACCESS_KEY.getMessage(),
				jwtProperties.getAccessExp());
		String refreshToken =
			jwtProperties.getPrefix() + EMPTY.getMessage() + generateToken(authId, role, REFRESH_KEY.getMessage(),
				jwtProperties.getRefreshExp());

		refreshTokenRepository.save(RefreshToken.builder()
			.id(authId)
			.refreshToken(refreshToken)
			.ttl(jwtProperties.getRefreshExp())
			.expiredAt(getExpiredTime())
			.build()
		);

		return new TokenResponseDto(accessToken, refreshToken, getExpiredTime());
	}

	private String generateToken(String authId, String role, String type, Long exp) {
		return Jwts.builder()
			.header()
			.add(TYPE.message, type).and()
			.claim(ROLE.getMessage(), role)
			.claim(AUTH_ID.getMessage(), authId)
			.signWith(jwtProperties.getSecret())
			.expiration(
				new Date(System.currentTimeMillis() + exp * 1000)
			)
			.compact();
	}

	public ZonedDateTime getExpiredTime() {
		return ZonedDateTime.now().plusSeconds(jwtProperties.getRefreshExp());
	}
}
