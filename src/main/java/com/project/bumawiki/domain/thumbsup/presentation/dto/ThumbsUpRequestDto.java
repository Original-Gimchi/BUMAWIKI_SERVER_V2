package com.project.bumawiki.domain.thumbsup.presentation.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public record ThumbsUpRequestDto(
	@NotNull
	Long docsId
) {
}
