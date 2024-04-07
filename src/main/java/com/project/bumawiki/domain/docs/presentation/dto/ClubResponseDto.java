package com.project.bumawiki.domain.docs.presentation.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.bumawiki.domain.docs.presentation.dto.response.DocsNameAndEnrollResponseDto;

public class ClubResponseDto {
	@JsonProperty
	private List<DocsNameAndEnrollResponseDto> club;
	@JsonProperty
	private List<DocsNameAndEnrollResponseDto> freeClub;

	public ClubResponseDto(List<DocsNameAndEnrollResponseDto> club, List<DocsNameAndEnrollResponseDto> freeClub) {
		this.club = club;
		this.freeClub = freeClub;
	}
}
