package com.project.bumawiki.domain.thumbsup.presentation.dto;

import jakarta.validation.constraints.NotNull;

public record ThumbsUpRequestDto(
	@NotNull
	Long docsId
) {
}
