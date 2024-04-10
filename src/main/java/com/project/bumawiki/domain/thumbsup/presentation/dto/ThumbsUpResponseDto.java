package com.project.bumawiki.domain.thumbsup.presentation.dto;

import com.project.bumawiki.domain.docs.domain.Docs;
import com.project.bumawiki.domain.docs.domain.type.DocsType;

public record ThumbsUpResponseDto(
	String title,
	DocsType docsType
) {

	public ThumbsUpResponseDto(Docs docs) {
		this(
			docs.getTitle(),
			docs.getDocsType()
		);
	}
}
