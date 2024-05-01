package com.project.bumawiki.domain.user.presentation.dto.request;

import com.project.bumawiki.domain.user.domain.authority.Authority;

import jakarta.validation.constraints.NotNull;

public record UserAuthorityRequestDto(
	@NotNull(message = "id는 null일 수 없습니다.")
	Long id,
	@NotNull(message = "authority는 null일 수 없습니다.")
	Authority authority
) {
}
