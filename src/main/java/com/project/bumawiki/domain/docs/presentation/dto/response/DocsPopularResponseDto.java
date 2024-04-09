package com.project.bumawiki.domain.docs.presentation.dto.response;

import com.project.bumawiki.domain.docs.domain.type.DocsType;

public record DocsPopularResponseDto(
	String title,
	Integer enroll,
	DocsType docsType,
	Long thumbsUpsCounts
) {

	public DocsPopularResponseDto(String title, Integer enroll, DocsType docsType, Long thumbsUpsCounts) {
		this.title = title;
		this.enroll = enroll;
		this.docsType = docsType;
		this.thumbsUpsCounts = thumbsUpsCounts;
	}
}
