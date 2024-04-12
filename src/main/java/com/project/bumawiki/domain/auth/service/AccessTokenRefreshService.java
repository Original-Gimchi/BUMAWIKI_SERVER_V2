package com.project.bumawiki.domain.auth.service;

import org.springframework.stereotype.Service;

import com.project.bumawiki.domain.auth.domain.Token;
import com.project.bumawiki.domain.auth.service.implementation.AuthReader;
import com.project.bumawiki.domain.auth.service.implementation.AuthValidator;
import com.project.bumawiki.domain.auth.service.implementation.TokenProvider;
import com.project.bumawiki.domain.auth.util.BearerTokenExtractor;
import com.project.bumawiki.domain.user.domain.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@SuppressWarnings("checkstyle:RegexpSinglelineJava")
@RequiredArgsConstructor
@Service
public class AccessTokenRefreshService {
	private final TokenProvider tokenProvider;
	private final AuthValidator authValidator;
	private final AuthReader authReader;
	//TODO UserReader로 변경
	private final UserRepository userRepository;

	public Token execute(String bearer) {
		String refreshToken = BearerTokenExtractor.extract(bearer);
		authValidator.shouldRefreshTokenValid(refreshToken);

		Long userId = authReader.getIdFromJwt(refreshToken);
		String accessToken = tokenProvider.createAccessToken(userRepository.getById(userId));

		return new Token(accessToken, refreshToken);
	}
}
