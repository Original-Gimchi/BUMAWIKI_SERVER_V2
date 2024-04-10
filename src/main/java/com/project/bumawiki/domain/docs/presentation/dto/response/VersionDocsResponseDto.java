package com.project.bumawiki.domain.docs.presentation.dto.response;

import java.time.LocalDateTime;

public record VersionDocsResponseDto(
	LocalDateTime thisVersionCreatedAt,
	Long userId,
	String nickName,
	int index
) {
}
