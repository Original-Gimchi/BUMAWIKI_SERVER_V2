package com.project.bumawiki.domain.docs.presentation.dto;

import java.time.LocalDateTime;

import com.project.bumawiki.domain.docs.domain.VersionDocs;
import com.project.bumawiki.domain.user.domain.User;

import lombok.Getter;

@Getter
public class VersionDocsSummaryDto {
	private final Integer version;
	private final LocalDateTime thisVersionCreatedAt;
	private final Long userId;
	private final String nickName;

	public VersionDocsSummaryDto(VersionDocs versionDocs) {
		User contributor = versionDocs.getUser();
		this.version = versionDocs.getVersion();
		this.thisVersionCreatedAt = versionDocs.getCreatedAt();
		this.nickName = contributor.getNickName();
		this.userId = contributor.getId();
	}
}
