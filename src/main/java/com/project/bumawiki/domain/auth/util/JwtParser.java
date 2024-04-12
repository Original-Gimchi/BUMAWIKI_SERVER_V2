package com.project.bumawiki.domain.auth.util;

import org.springframework.stereotype.Component;

import com.project.bumawiki.global.config.jwt.JwtCredentials;
import com.project.bumawiki.global.error.exception.BumawikiException;
import com.project.bumawiki.global.error.exception.ErrorCode;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtParser {
	public static final String ID = "id";
	private final JwtCredentials jwtCredentials;

	public Long getIdFromJwt(String jwt) {
		try {
			return Long.parseLong(
				Jwts.parser()
					.verifyWith(jwtCredentials.secretKey())
					.build()
					.parseSignedClaims(jwt)
					.getPayload()
					.get(ID)
					.toString()
			);
		} catch (ExpiredJwtException e) {
			throw new BumawikiException(ErrorCode.EXPIRED_JWT);
		} catch (JwtException e) {
			throw new BumawikiException(ErrorCode.INVALID_JWT);
		}
	}
}
