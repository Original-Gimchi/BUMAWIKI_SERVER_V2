package com.project.bumawiki.domain.user.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.bumawiki.domain.auth.domain.repository.AuthIdRepository;
import com.project.bumawiki.domain.auth.domain.repository.RefreshTokenRepository;
import com.project.bumawiki.global.jwt.properties.JwtConstants;
import com.project.bumawiki.global.jwt.util.JwtUtil;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserLogoutService {

	private final JwtUtil jwtUtil;
	private final AuthIdRepository authIdRepository;
	private final RefreshTokenRepository refreshTokenRepository;

	@Transactional
	public String execute(String bearerRefreshToken) {
		String authId = jwtUtil.getJwtBody(bearerRefreshToken).get(JwtConstants.AUTH_ID.message).toString();

		authIdRepository.findByAuthId(authId)
			.ifPresent(authIdRepository::delete);

		refreshTokenRepository.findById(authId)
			.ifPresent(refreshTokenRepository::delete);

		SecurityContextHolder.clearContext();

		return authId;
	}
}
