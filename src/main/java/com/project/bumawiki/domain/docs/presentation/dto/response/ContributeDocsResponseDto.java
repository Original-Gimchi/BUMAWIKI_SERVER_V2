package com.project.bumawiki.domain.docs.presentation.dto.response;

import java.time.LocalDateTime;

import com.project.bumawiki.domain.docs.domain.VersionDocs;

public record ContributeDocsResponseDto(
	Long userId,
	String userNickName,
	Long docsId,
	LocalDateTime createTime,
	String title,
	Integer version
	) {
	public ContributeDocsResponseDto(VersionDocs versionDocs) {
		this(
			versionDocs.getUser().getId(),
			versionDocs.getUser().getNickName(),
			versionDocs.getDocs().getId(),
			versionDocs.getCreatedAt(),
			versionDocs.getDocs().getTitle(),
			versionDocs.getVersion()
		);
	}
}
