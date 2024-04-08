package com.project.bumawiki.domain.user.domain.repository;

import org.springframework.stereotype.Repository;

import com.project.bumawiki.domain.user.domain.User;
import com.project.bumawiki.domain.user.exception.UserNotFoundException;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserRepositoryMapper {

	private final UserRepository userRepository;

	public User getByEmail(final String email) {
		return userRepository.findByEmail(email)
			.orElseThrow(() -> UserNotFoundException.EXCEPTION);
	}

	public User getById(final Long userId) {
		return userRepository.findById(userId)
			.orElseThrow(() -> UserNotFoundException.EXCEPTION);
	}

}
