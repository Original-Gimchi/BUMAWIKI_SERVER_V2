package com.project.bumawiki.domain.docs.presentation.dto.response;

import java.time.LocalDateTime;

import com.project.bumawiki.domain.docs.domain.VersionDocs;

public record VersionDocsSummaryResponseDto(
	Integer version,
	LocalDateTime thisVersionCreatedAt,
	Long userId,
	String nickName
) {
	public VersionDocsSummaryResponseDto(VersionDocs versionDocs) {
		this(
			versionDocs.getVersion(),
			versionDocs.getCreatedAt(),
			versionDocs.getUser().getId(),
			versionDocs.getUser().getNickName()
		);
	}
}
