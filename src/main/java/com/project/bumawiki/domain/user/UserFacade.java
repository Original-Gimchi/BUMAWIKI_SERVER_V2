package com.project.bumawiki.domain.user;

import org.springframework.stereotype.Component;

import com.project.bumawiki.domain.user.domain.User;
import com.project.bumawiki.domain.user.domain.repository.UserRepository;
import com.project.bumawiki.domain.user.exception.UserNotFoundException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserFacade {
	private final UserRepository userRepository;

	public User getUserByEmail(String email) {
		return userRepository.findByEmail(email)
			.orElseThrow(() -> UserNotFoundException.EXCEPTION);
	}
}
