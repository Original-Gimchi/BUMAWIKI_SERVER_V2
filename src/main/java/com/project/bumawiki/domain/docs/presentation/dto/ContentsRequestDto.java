package com.project.bumawiki.domain.docs.presentation.dto;

import jakarta.validation.constraints.NotNull;

public record ContentsRequestDto(
	@NotNull(message = "내용을 입력해주세요.")
	String contents
) {
}
