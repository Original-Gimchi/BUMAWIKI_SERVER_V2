package com.project.bumawiki.global.jwt.auth;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.project.bumawiki.global.config.security.auth.AuthDetailsService;
import com.project.bumawiki.global.jwt.exception.InvalidJwtException;
import com.project.bumawiki.global.jwt.properties.JwtConstants;
import com.project.bumawiki.global.jwt.util.JwtUtil;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuth {

	private final JwtUtil jwtUtil;
	private final AuthDetailsService authDetailsService;

	public Authentication authentication(String token) {
		Claims claims = jwtUtil.getJwt(token).getPayload();

		if (isNotAccessToken(token)) {
			throw InvalidJwtException.EXCEPTION;
		}

		UserDetails userDetails = authDetailsService.loadUserByUsername(
			claims.get(JwtConstants.AUTH_ID.message).toString());
		return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
	}

	private boolean isNotAccessToken(String token) {
		if (token.isEmpty()) {
			throw InvalidJwtException.EXCEPTION;
		}
		String role = jwtUtil.getJwt(token).getHeader().get(JwtConstants.TYPE.message).toString();
		return !role.equals(JwtConstants.ACCESS_KEY.message);
	}
}
