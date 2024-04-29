package com.project.bumawiki.domain.file.presentation.dto;


public record R2FileResponseDto(
	String url
) {
	public static R2FileResponseDto from(String url) {
		return new R2FileResponseDto(url);
	}
}
