package com.project.bumawiki.domain.auth.implementation;

import org.springframework.stereotype.Service;

import com.project.bumawiki.domain.auth.repository.AuthRepository;
import com.project.bumawiki.domain.auth.util.JwtParser;
import com.project.bumawiki.domain.user.domain.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthReader {
	private final AuthRepository authRepository;
	private final JwtParser jwtParser;

	public Long getIdFromJwt(String token) {
		return jwtParser.getIdFromJwt(token);
	}

	public User getCurrentUser() {
		return authRepository.getCurrentUser();
	}

	public User getNullableCurrentUser() {
		return authRepository.getNullableCurrentUser();
	}
}
