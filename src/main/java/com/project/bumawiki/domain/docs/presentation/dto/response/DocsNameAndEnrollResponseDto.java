package com.project.bumawiki.domain.docs.presentation.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.project.bumawiki.domain.docs.domain.Docs;
import com.project.bumawiki.domain.docs.domain.VersionDocs;
import com.project.bumawiki.domain.docs.domain.type.DocsType;
import com.project.bumawiki.domain.docs.util.DocsUtil;

import lombok.Getter;

@Getter
public class DocsNameAndEnrollResponseDto {

	private final Long id;
	private final String title;
	private final int enroll;
	private final String simpleContents;
	private final DocsType docsType;
	private final LocalDateTime lastModifiedAt;
	private final String thumbnail;

	public DocsNameAndEnrollResponseDto(Docs docs) {
		this.id = docs.getId();
		this.title = docs.getTitle();
		this.enroll = docs.getEnroll();
		this.docsType = docs.getDocsType();
		this.lastModifiedAt = docs.getLastModifiedAt();
		this.simpleContents = getSimpleContents(docs);
		this.thumbnail = DocsUtil.getThumbnail(getContents(docs));
	}

	private String getSimpleContents(Docs docs) {
		String contents = getContents(docs);
		int endIndex = Math.min(contents.length(), 200);
		return contents.substring(0, endIndex);
	}

	private String getContents(Docs docs) {
		List<VersionDocs> docsVersion = docs.getDocsVersion();
		int currentDocsSize = docsVersion.size() - 1;
		return docsVersion.get(currentDocsSize).getContents();
	}
}
