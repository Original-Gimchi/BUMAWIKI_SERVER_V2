package com.project.bumawiki.domain.auth.presentation.dto.request;

import jakarta.validation.constraints.NotNull;

public record LoginRequestDto(
	@NotNull(message = "authCode는 null일 수 없습니다.")
	String authCode
) {
}
