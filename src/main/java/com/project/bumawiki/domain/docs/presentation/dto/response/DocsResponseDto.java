package com.project.bumawiki.domain.docs.presentation.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.project.bumawiki.domain.docs.domain.Docs;
import com.project.bumawiki.domain.docs.domain.VersionDocs;
import com.project.bumawiki.domain.docs.domain.type.DocsType;
import com.project.bumawiki.domain.docs.domain.type.Status;
import com.project.bumawiki.domain.docs.util.DocsUtil;
import com.project.bumawiki.domain.user.domain.User;
import com.project.bumawiki.domain.user.presentation.dto.SimpleUserDto;

public record DocsResponseDto(
	Long id,
	String title,
	String contents,
	DocsType docsType,
	LocalDateTime lastModifiedAt,
	int enroll,
	boolean isDocsDetail,
	List<SimpleUserDto> contributors,
	Status status,
	int version,
	String thumbnail
) {

	public DocsResponseDto(Docs docs, List<User> contributors, VersionDocs versionDocs) {
		this(
			docs.getId(),
			docs.getTitle(),
			versionDocs.getContents(),
			docs.getDocsType(),
			docs.getLastModifiedAt(),
			docs.getEnroll(),
			true,
			contributors.stream()
				.map(SimpleUserDto::new)
				.toList(),
			docs.getStatus(),
			versionDocs.getVersion(),
			DocsUtil.getThumbnail(versionDocs.getContents())
		);
	}
}

