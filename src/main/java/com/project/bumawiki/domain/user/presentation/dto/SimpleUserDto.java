package com.project.bumawiki.domain.user.presentation.dto;

import com.project.bumawiki.domain.user.domain.User;

public record SimpleUserDto(
	Long id,
	String email,
	String nickName,
	String name
) {

	public SimpleUserDto(User user) {
		this(
			user.getId(),
			user.getEmail(),
			user.getNickName(),
			user.getName()
		);
	}
}
