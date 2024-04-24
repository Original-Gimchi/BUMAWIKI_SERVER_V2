package com.project.bumawiki.domain.user.presentation.dto;

import com.project.bumawiki.domain.user.domain.authority.Authority;

import jakarta.validation.constraints.NotNull;

public record UserAuthorityRequestDto(
	@NotNull Long id,
	@NotNull Authority authority
) {
}
