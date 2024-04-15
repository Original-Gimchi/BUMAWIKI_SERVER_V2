package com.project.bumawiki.domain.user.implementation;

import com.project.bumawiki.domain.user.domain.User;
import com.project.bumawiki.domain.user.domain.repository.UserRepository;
import com.project.bumawiki.global.annotation.Implementation;
import com.project.bumawiki.global.error.exception.BumawikiException;
import com.project.bumawiki.global.error.exception.ErrorCode;

import lombok.RequiredArgsConstructor;

@Implementation
@RequiredArgsConstructor
public class UserReader {
	private final UserRepository userRepository;

	public User getById(Long id) {
		return userRepository.findById(id).orElseThrow(() -> new BumawikiException(ErrorCode.USER_NOT_FOUND));
	}

	public User getNullableUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}
}
