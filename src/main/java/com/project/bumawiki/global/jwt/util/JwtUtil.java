package com.project.bumawiki.global.jwt.util;

import static com.project.bumawiki.global.jwt.properties.JwtConstants.*;

import java.util.Objects;

import org.springframework.stereotype.Component;

import com.project.bumawiki.domain.auth.domain.repository.AuthIdRepository;
import com.project.bumawiki.global.jwt.exception.ExpiredJwtException;
import com.project.bumawiki.global.jwt.exception.InvalidJwtException;
import com.project.bumawiki.global.jwt.properties.JwtProperties;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtUtil {

	private final JwtProperties jwtProperties;
	private final AuthIdRepository authIdRepository;

	public String resolveToken(HttpServletRequest request) {
		String bearer = request.getHeader(jwtProperties.getHeader());

		return parseToken(bearer);
	}

	public String parseToken(String bearer) {
		if (!Objects.equals(bearer, "") && bearer != null) {
			String token = bearer.replaceAll(jwtProperties.getPrefix(), "").trim();
			checkingIfJwtExpired(token);
			return token;
		}
		return null;
	}

	public void checkingIfJwtExpired(String token) {
		String authId = getJwt(token).getPayload().get(AUTH_ID.getMessage()).toString();

		authIdRepository.findByAuthId(authId)
			.orElseThrow(() -> ExpiredJwtException.EXCEPTION);
	}

	public Jws<Claims> getJwt(String token) {
		if (token == null) {
			throw InvalidJwtException.EXCEPTION;
		}
		return Jwts.parser().verifyWith(jwtProperties.getSecret()).build().parseSignedClaims(token);
	}

	public Claims getJwtBody(String bearer) {
		Jws<Claims> jwt = getJwt(parseToken(bearer));
		return jwt.getPayload();
	}
}
