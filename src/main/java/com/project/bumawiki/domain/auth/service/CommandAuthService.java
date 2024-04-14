package com.project.bumawiki.domain.auth.service;

import org.springframework.stereotype.Service;

import com.project.bumawiki.domain.auth.domain.Token;
import com.project.bumawiki.domain.auth.implementation.AuthReader;
import com.project.bumawiki.domain.auth.implementation.AuthValidator;
import com.project.bumawiki.domain.auth.implementation.TokenProvider;
import com.project.bumawiki.domain.auth.infra.BsmLoginHandler;
import com.project.bumawiki.domain.auth.util.BearerTokenExtractor;
import com.project.bumawiki.domain.user.domain.User;
import com.project.bumawiki.domain.user.implementation.UserCreator;
import com.project.bumawiki.domain.user.implementation.UserReader;
import com.project.bumawiki.domain.user.implementation.UserUpdater;

import leehj050211.bsmOauth.dto.response.BsmResourceResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommandAuthService {
	private final AuthValidator authValidator;
	private final AuthReader authReader;
	private final BsmLoginHandler bsmLoginHandler;
	private final TokenProvider tokenProvider;
	private final UserReader userReader;
	private final UserCreator userCreator;
	private final UserUpdater userUpdater;

	public Token login(String authId) {
		BsmResourceResponse resource = bsmLoginHandler.getResource(authId);
		User user = userReader.getByEmail(resource.getEmail());

		if (user == null) {
			userCreator.create(resource);
		} else {
			userUpdater.update(user, resource);
		}

		return tokenProvider.createNewTokens(user);
	}

	public Token refresh(String bearer) {
		String refreshToken = BearerTokenExtractor.extract(bearer);
		authValidator.shouldRefreshTokenValid(refreshToken);

		Long userId = authReader.getIdFromJwt(refreshToken);
		String accessToken = tokenProvider.createAccessToken(userReader.getById(userId));

		return new Token(accessToken, refreshToken);
	}
}
