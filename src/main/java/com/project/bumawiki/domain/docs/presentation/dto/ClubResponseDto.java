package com.project.bumawiki.domain.docs.presentation.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.bumawiki.domain.docs.domain.Docs;
import com.project.bumawiki.domain.docs.presentation.dto.response.DocsNameAndEnrollResponseDto;

public class ClubResponseDto {
	@JsonProperty
	private List<DocsNameAndEnrollResponseDto> club;
	@JsonProperty
	private List<DocsNameAndEnrollResponseDto> freeClub;

	public ClubResponseDto(List<Docs> club, List<Docs> freeClub) {
		this.club = club.stream()
			.map(DocsNameAndEnrollResponseDto::new)
			.collect(Collectors.toList());
		this.freeClub = freeClub.stream()
			.map(DocsNameAndEnrollResponseDto::new)
			.collect(Collectors.toList());
	}
}
