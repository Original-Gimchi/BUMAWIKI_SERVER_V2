package com.project.bumawiki.domain.user.service;

import java.io.IOException;

import com.project.bumawiki.domain.auth.domain.AuthId;
import com.project.bumawiki.domain.auth.domain.repository.AuthIdRepository;
import com.project.bumawiki.domain.auth.service.UserSignUpOrUpdateService;
import com.project.bumawiki.domain.user.domain.User;
import com.project.bumawiki.global.annotation.ServiceWithTransactionalReadOnly;
import com.project.bumawiki.global.jwt.dto.TokenResponseDto;
import com.project.bumawiki.global.jwt.properties.JwtProperties;
import com.project.bumawiki.global.jwt.util.JwtProvider;

import lombok.RequiredArgsConstructor;

@ServiceWithTransactionalReadOnly
@RequiredArgsConstructor
public class UserLoginService {
	private final UserSignUpOrUpdateService userSignUpOrUpdateService;
	private final JwtProvider jwtProvider;
	private final JwtProperties jwtProperties;
	private final AuthIdRepository authIdRepository;

	public TokenResponseDto execute(String authId) throws IOException {

		User user = userSignUpOrUpdateService.execute(authId);
		saveAuthId(user.getEmail());

		return jwtProvider.generateToken(user.getEmail(), user.getAuthority().name());
	}

	private void saveAuthId(String email) {
		authIdRepository.save(
			AuthId.builder()
				.id(email)
				.authId(email)
				.ttl(jwtProperties.getRefreshExp())
				.build()
		);
	}
}
