package com.project.bumawiki.domain.thumbsup.presentation.dto;

public record ThumbsUpCountResponseDto(
	Long thumbsUpsCount
) {

	public ThumbsUpCountResponseDto(Long thumbsUpsCount) {
		this.thumbsUpsCount = thumbsUpsCount;
	}
}
