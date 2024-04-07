package com.project.bumawiki.domain.user.presentation.dto;

import com.project.bumawiki.domain.user.domain.User;

import lombok.Getter;

@Getter
public class SimpleUserDto {
	private final Long id;

	private final String email;

	private final String nickName;

	private final String name;

	public SimpleUserDto(User user) {
		this.id = user.getId();
		this.email = user.getEmail();
		this.nickName = user.getNickName();
		this.name = user.getName();
	}
}
