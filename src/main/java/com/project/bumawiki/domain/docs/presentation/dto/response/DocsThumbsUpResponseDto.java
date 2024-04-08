package com.project.bumawiki.domain.docs.presentation.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DocsThumbsUpResponseDto {

	@JsonProperty
	private final int thumbsUpsCount;

	public DocsThumbsUpResponseDto(int thumbsUpsCount) {
		this.thumbsUpsCount = thumbsUpsCount;
	}
}
