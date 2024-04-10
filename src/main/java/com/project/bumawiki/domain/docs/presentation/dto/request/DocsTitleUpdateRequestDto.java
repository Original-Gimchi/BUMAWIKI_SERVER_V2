package com.project.bumawiki.domain.docs.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;

public record DocsTitleUpdateRequestDto(
	@NotBlank
	String title
) {
}
