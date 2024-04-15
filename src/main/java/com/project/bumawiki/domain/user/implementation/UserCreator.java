package com.project.bumawiki.domain.user.implementation;

import com.project.bumawiki.domain.user.domain.User;
import com.project.bumawiki.domain.user.domain.repository.UserRepository;
import com.project.bumawiki.global.annotation.Implementation;

import lombok.RequiredArgsConstructor;

@Implementation
@RequiredArgsConstructor
public class UserCreator {
	private final UserRepository userRepository;

	public void create(User user) {
		userRepository.save(user);
	}
}
