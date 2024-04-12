package com.project.bumawiki.domain.auth.service;

import java.io.IOException;
import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;

import com.project.bumawiki.domain.auth.domain.Token;
import com.project.bumawiki.domain.auth.service.implementation.TokenProvider;
import com.project.bumawiki.domain.user.domain.User;
import com.project.bumawiki.domain.user.domain.authority.Authority;
import com.project.bumawiki.domain.user.domain.repository.UserRepository;
import com.project.bumawiki.global.annotation.ServiceWithTransactionalReadOnly;
import com.project.bumawiki.global.error.exception.BumawikiException;
import com.project.bumawiki.global.error.exception.ErrorCode;

import leehj050211.bsmOauth.BsmOauth;
import leehj050211.bsmOauth.dto.response.BsmResourceResponse;
import leehj050211.bsmOauth.exceptions.BsmAuthCodeNotFoundException;
import leehj050211.bsmOauth.exceptions.BsmAuthInvalidClientException;
import leehj050211.bsmOauth.exceptions.BsmAuthTokenNotFoundException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@ServiceWithTransactionalReadOnly
public class UserSignUpOrUpdateService {
	private final BsmOauth bsmOauth;
	private final UserRepository userRepository;
	private final TokenProvider tokenProvider;

	@Transactional
	public Token execute(String authId) throws IOException {
		String token;
		BsmResourceResponse resource;
		try {
			token = bsmOauth.getToken(authId);
			resource = bsmOauth.getResource(token);
		} catch (BsmAuthCodeNotFoundException | BsmAuthTokenNotFoundException | BsmAuthInvalidClientException e) {
			throw new BumawikiException(ErrorCode.USER_NOT_LOGIN);
		}
		User user = updateOrSignUp(resource);

		return tokenProvider.createNewTokens(user);
	}

	protected User updateOrSignUp(BsmResourceResponse resource) {
		Optional<User> user = userRepository.findByEmail(resource.getEmail());
		if (user.isEmpty()) {
			return saveUser(resource);
		}
		User updateUser = user.get();
		return updateUser.update(resource);
	}

	protected User saveUser(final BsmResourceResponse resource) {
		return userRepository.save(
			User.builder()
				.email(resource.getEmail())
				.nickName(resource.getNickname())
				.authority(Authority.USER)
				.enroll(resource.getStudent().getEnrolledAt())
				.name(resource.getStudent().getName())
				.build()
		);
	}
}
