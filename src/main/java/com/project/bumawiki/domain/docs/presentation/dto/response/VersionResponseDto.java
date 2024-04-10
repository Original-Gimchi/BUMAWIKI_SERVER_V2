package com.project.bumawiki.domain.docs.presentation.dto.response;

import java.util.List;

import com.project.bumawiki.domain.docs.domain.Docs;
import com.project.bumawiki.domain.docs.domain.type.DocsType;

public record VersionResponseDto(
	int length,
	List<VersionDocsResponseDto> versionDocsResponseDto,
	DocsType docsType,
	String title
) {

	public VersionResponseDto(List<VersionDocsResponseDto> versionDocsResponseDto, Docs findDocs) {
		this(
			versionDocsResponseDto.size(),
			versionDocsResponseDto,
			findDocs.getDocsType(),
			findDocs.getTitle()
		);
	}
}
