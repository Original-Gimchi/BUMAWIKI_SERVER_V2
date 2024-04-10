package com.project.bumawiki.domain.docs.presentation.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.project.bumawiki.domain.docs.domain.Docs;
import com.project.bumawiki.domain.docs.domain.VersionDocs;
import com.project.bumawiki.domain.docs.domain.type.DocsType;
import com.project.bumawiki.domain.docs.util.DocsUtil;

public record DocsNameAndEnrollResponseDto(
	Long id,
	String title,
	int enroll,
	String simpleContents,
	DocsType docsType,
	LocalDateTime lastModifiedAt,
	String thumbnail
) {

	public DocsNameAndEnrollResponseDto(Docs docs) {
		this(
			docs.getId(),
			docs.getTitle(),
			docs.getEnroll(),
			getSimpleContents(docs),
			docs.getDocsType(),
			docs.getLastModifiedAt(),
			DocsUtil.getThumbnail(getContents(docs))
		);
	}

	private static String getSimpleContents(Docs docs) {
		String contents = getContents(docs);
		int endIndex = Math.min(contents.length(), 200);
		return contents.substring(0, endIndex);
	}

	private static String getContents(Docs docs) {
		List<VersionDocs> docsVersion = docs.getVersionDocs();
		int currentDocsSize = docsVersion.size() - 1;
		return docsVersion.get(currentDocsSize).getContents();
	}
}
