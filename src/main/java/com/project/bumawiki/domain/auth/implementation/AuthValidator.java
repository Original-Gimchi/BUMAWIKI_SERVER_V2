package com.project.bumawiki.domain.auth.implementation;

import org.springframework.stereotype.Service;

import com.project.bumawiki.global.config.jwt.JwtCredentials;
import com.project.bumawiki.global.error.exception.BumawikiException;
import com.project.bumawiki.global.error.exception.ErrorCode;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthValidator {
	private final JwtCredentials jwtCredentials;

	public void shouldRefreshTokenValid(String refreshToken) {
		try {
			Jwts.parser()
				.verifyWith(jwtCredentials.secretKey())
				.build()
				.parse(refreshToken);
		} catch (ExpiredJwtException e) {
			throw new BumawikiException(ErrorCode.EXPIRED_JWT);
		} catch (JwtException e) {
			throw new BumawikiException(ErrorCode.INVALID_JWT);
		}
	}
}
