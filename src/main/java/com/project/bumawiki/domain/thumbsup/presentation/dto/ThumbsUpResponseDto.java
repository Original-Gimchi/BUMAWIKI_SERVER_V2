package com.project.bumawiki.domain.thumbsup.presentation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.bumawiki.domain.docs.domain.Docs;
import com.project.bumawiki.domain.docs.domain.type.DocsType;

public class ThumbsUpResponseDto {

	@JsonProperty
	private final String title;
	@JsonProperty
	private final DocsType docsType;

	public ThumbsUpResponseDto(Docs docs) {
		this.title = docs.getTitle();
		this.docsType = docs.getDocsType();
	}
}
