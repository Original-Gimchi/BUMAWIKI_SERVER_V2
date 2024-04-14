package com.project.bumawiki.domain.docs.presentation.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.project.bumawiki.domain.docs.domain.Docs;
import com.project.bumawiki.domain.docs.domain.VersionDocs;
import com.project.bumawiki.domain.docs.domain.type.DocsType;
import com.project.bumawiki.domain.docs.service.DocsUtil;
import com.project.bumawiki.domain.user.domain.User;
import com.project.bumawiki.domain.user.presentation.dto.SimpleUserDto;

import lombok.Getter;

@Getter
public class DocsResponseDto {

	private final Long id;
	private final String title;
	private final String contents;
	private final DocsType docsType;
	private final LocalDateTime lastModifiedAt;
	private final int enroll;
	private final boolean isDocsDetail;
	private final List<SimpleUserDto> contributors;
	private final int version;
	private final String thumbnail;

	public DocsResponseDto(Docs docs, List<User> contributors) {
		int lastValueOfDocsVersion = docs.getDocsVersion().size() - 1;
		VersionDocs versionDocs = docs.getDocsVersion().get(lastValueOfDocsVersion);

		this.id = docs.getId();
		this.title = docs.getTitle();
		this.contents = versionDocs.getContents();
		this.lastModifiedAt = docs.getLastModifiedAt();
		this.docsType = docs.getDocsType();
		this.enroll = docs.getEnroll();
		this.isDocsDetail = true;
		this.contributors = contributors.stream()
			.map(SimpleUserDto::new)
			.collect(Collectors.toList());
		this.version = docs.getLastVersion();
		this.thumbnail = DocsUtil.getThumbnail(versionDocs.getContents());
	}
}

