package com.project.bumawiki.domain.auth.implementation;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.project.bumawiki.domain.auth.domain.Token;
import com.project.bumawiki.domain.user.domain.User;
import com.project.bumawiki.global.config.jwt.JwtCredentials;

import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TokenProvider {
	private final JwtCredentials jwtCredentials;

	public Token createNewTokens(User user) {
		return new Token(
			createAccessToken(user),
			createRefreshToken(user)
		);
	}

	public String createAccessToken(User user) {
		return createToken(user, jwtCredentials.accessTokenExpirationTime());
	}

	private String createRefreshToken(User user) {
		return createToken(user, jwtCredentials.refreshTokenExpirationTime());
	}

	private String createToken(User user, long expireLength) {
		Date now = new Date();
		Date expiration = new Date(System.currentTimeMillis() + expireLength);
		return Jwts.builder()
			.claim("id", user.getId())
			.expiration(expiration)
			.signWith(jwtCredentials.secretKey())
			.compact();
	}
}
