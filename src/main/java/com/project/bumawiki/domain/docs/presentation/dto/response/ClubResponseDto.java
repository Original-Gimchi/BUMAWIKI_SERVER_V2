package com.project.bumawiki.domain.docs.presentation.dto.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.bumawiki.domain.docs.domain.Docs;

public record ClubResponseDto(
	@JsonProperty
	List<DocsNameAndEnrollResponseDto> club,
	@JsonProperty
	List<DocsNameAndEnrollResponseDto> freeClub
) {
	public static ClubResponseDto from(List<Docs> club, List<Docs> freeClub) {
		return new ClubResponseDto(
			convertToDtoList(club),
			convertToDtoList(freeClub)
		);
	}

	private static List<DocsNameAndEnrollResponseDto> convertToDtoList(List<Docs> docs) {
		return docs.stream()
			.map(DocsNameAndEnrollResponseDto::new)
			.toList();
	}
}

