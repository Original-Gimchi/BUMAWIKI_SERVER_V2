package com.project.bumawiki.domain.file.presentation.dto;

import org.springframework.core.io.Resource;

public record FileResponseDto(
	Resource resource,
	String contentType
) {
}
