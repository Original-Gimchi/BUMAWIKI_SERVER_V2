package com.project.bumawiki.domain.user.presentation.dto;

import com.project.bumawiki.domain.user.domain.authority.Authority;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserAuthorityDto {

	@NotNull
	private String email;
	@NotNull
	private Authority authority;

}
