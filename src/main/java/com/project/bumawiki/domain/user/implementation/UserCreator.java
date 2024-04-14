package com.project.bumawiki.domain.user.implementation;

import com.project.bumawiki.domain.user.domain.User;
import com.project.bumawiki.domain.user.domain.authority.Authority;
import com.project.bumawiki.domain.user.domain.repository.UserRepository;
import com.project.bumawiki.global.annotation.Implementation;

import leehj050211.bsmOauth.dto.response.BsmResourceResponse;
import lombok.RequiredArgsConstructor;

@Implementation
@RequiredArgsConstructor
public class UserCreator {
	private final UserRepository userRepository;

	public void create(BsmResourceResponse resource) {
		userRepository.save(
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
