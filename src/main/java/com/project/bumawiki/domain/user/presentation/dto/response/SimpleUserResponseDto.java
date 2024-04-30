package com.project.bumawiki.domain.user.presentation.dto.response;

import com.project.bumawiki.domain.user.domain.User;

public record SimpleUserResponseDto(
	Long id,
	String email,
	String nickName,
	String name
) {

	public SimpleUserResponseDto(User user) {
		this(
			user.getId(),
			user.getEmail(),
			user.getNickName(),
			user.getName()
		);
	}
}
