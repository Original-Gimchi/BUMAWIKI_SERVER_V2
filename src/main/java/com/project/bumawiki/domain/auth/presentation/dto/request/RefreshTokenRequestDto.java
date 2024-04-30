package com.project.bumawiki.domain.auth.presentation.dto.request;

import jakarta.validation.constraints.NotNull;

public record RefreshTokenRequestDto(
	@NotNull(message = "refreshToken은 null일 수 없습니다.")
	String refreshToken
) {
}
