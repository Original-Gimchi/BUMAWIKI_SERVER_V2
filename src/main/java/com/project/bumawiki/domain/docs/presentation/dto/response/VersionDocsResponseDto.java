package com.project.bumawiki.domain.docs.presentation.dto.response;

import java.time.LocalDateTime;

import lombok.Getter;

@Getter
public final class VersionDocsResponseDto {
	private final LocalDateTime thisVersionCreatedAt;
	private final Long userId;
	private final String nickName;
	private int index;

	public VersionDocsResponseDto(LocalDateTime thisVersionCreatedAt, Long userId, String nickName) {
		this.thisVersionCreatedAt = thisVersionCreatedAt;
		this.userId = userId;
		this.nickName = nickName;
	}

	public void updateIndex(int index) {
		this.index = index;
	}

}
