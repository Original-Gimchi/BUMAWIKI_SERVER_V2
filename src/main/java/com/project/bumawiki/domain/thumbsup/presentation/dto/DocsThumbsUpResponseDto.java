package com.project.bumawiki.domain.thumbsup.presentation.dto;

public record DocsThumbsUpResponseDto(
	Long thumbsUpsCount
) {

	public DocsThumbsUpResponseDto(Long thumbsUpsCount) {
		this.thumbsUpsCount = thumbsUpsCount;
	}
}
