package com.project.bumawiki.domain.auth.service.implementation;

import org.springframework.stereotype.Service;

import com.project.bumawiki.domain.auth.repository.AuthRepository;
import com.project.bumawiki.domain.user.domain.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthUpdater {
	private final AuthRepository authRepository;

	public void updateCurrentUser(User user) {
		authRepository.updateCurrentUser(user);
	}
}
