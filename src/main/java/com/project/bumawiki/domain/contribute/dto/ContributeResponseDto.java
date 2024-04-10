package com.project.bumawiki.domain.contribute.dto;

import java.time.LocalDateTime;

import com.project.bumawiki.domain.docs.domain.VersionDocs;

import lombok.Getter;

@Getter
public class ContributeResponseDto {

	private final Long userId;
	private final String userNickName;
	private final Long docsId;
	private final LocalDateTime createTime;
	private final String title;
	private final Integer version;

	public ContributeResponseDto(VersionDocs versionDocs) {
		this.userId = versionDocs.getUser().getId();
		this.userNickName = versionDocs.getUser().getNickName();
		this.docsId = versionDocs.getDocs().getId();
		this.createTime = versionDocs.getCreatedAt();
		this.title = versionDocs.getDocs().getTitle();
		this.version = versionDocs.getVersion();
	}
}
