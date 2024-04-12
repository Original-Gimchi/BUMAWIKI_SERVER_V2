package com.project.bumawiki.domain.auth.service;

import org.springframework.stereotype.Service;

import com.project.bumawiki.domain.auth.repository.AuthRepository;
import com.project.bumawiki.domain.user.domain.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QueryAuthService {
	private final AuthRepository authRepository;

	public User getCurrentUser() {
		return authRepository.getCurrentUser();
	}
}
