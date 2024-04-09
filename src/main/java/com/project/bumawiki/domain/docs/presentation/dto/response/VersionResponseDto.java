package com.project.bumawiki.domain.docs.presentation.dto.response;

import java.util.List;

import com.project.bumawiki.domain.docs.domain.Docs;
import com.project.bumawiki.domain.docs.domain.type.DocsType;

import lombok.Getter;

@Getter
public class VersionResponseDto {
	private final int length;
	private final List<VersionDocsResponseDto> versionDocsResponseDto;
	private final DocsType docsType;
	private final String title;

	public VersionResponseDto(List<VersionDocsResponseDto> versionDocsResponseDto, Docs findDocs) {
		length = versionDocsResponseDto.size();
		this.versionDocsResponseDto = versionDocsResponseDto;
		this.docsType = findDocs.getDocsType();
		this.title = findDocs.getTitle();

		int index = length;
		for (VersionDocsResponseDto docsResponseDto : versionDocsResponseDto) {
			docsResponseDto.updateIndex(index);
			index -= 1;
		}
	}
}
