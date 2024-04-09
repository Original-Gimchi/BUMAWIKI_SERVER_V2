package com.project.bumawiki.domain.docs.presentation.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.bumawiki.domain.docs.domain.type.DocsType;

public class DocsPopularResponseDto {
	@JsonProperty
	private final String title;
	@JsonProperty
	private final int enroll;
	@JsonProperty
	private final DocsType docsType;
	@JsonProperty
	private final long thumbsUpsCounts;

	public DocsPopularResponseDto(String title, int enroll, DocsType docsType, long thumbsUpsCounts) {
		this.title = title;
		this.enroll = enroll;
		this.docsType = docsType;
		this.thumbsUpsCounts = thumbsUpsCounts;
	}
}
