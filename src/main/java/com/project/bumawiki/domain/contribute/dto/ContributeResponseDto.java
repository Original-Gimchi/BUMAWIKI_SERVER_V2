package com.project.bumawiki.domain.contribute.dto;

import java.time.LocalDateTime;

import com.project.bumawiki.domain.contribute.domain.Contribute;

import lombok.Getter;

@Getter
public class ContributeResponseDto {

	private final Long userId;
	private final String userNickName;
	private final Long docsId;
	private final LocalDateTime createTime;
	private final String title;
	private final Long versionDocsId;

	public ContributeResponseDto(Contribute contribute) {
		this.userId = contribute.getContributor().getId();
		this.userNickName = contribute.getContributor().getNickName();
		this.docsId = contribute.getDocs().getId();
		this.createTime = contribute.getCreatedAt();
		this.title = contribute.getDocs().getTitle();
		this.versionDocsId = contribute.getVersionDocs().getId();
	}
}
