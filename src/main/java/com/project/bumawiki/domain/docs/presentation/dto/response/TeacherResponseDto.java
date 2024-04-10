package com.project.bumawiki.domain.docs.presentation.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.bumawiki.domain.docs.domain.Docs;

import java.util.List;

public record TeacherResponseDto(
	@JsonProperty List<DocsNameAndEnrollResponseDto> teacher,
	@JsonProperty List<DocsNameAndEnrollResponseDto> majorTeacher,
	@JsonProperty List<DocsNameAndEnrollResponseDto> mentorTeacher
) {
	public static TeacherResponseDto from( List<Docs> teacher, List<Docs> majorTeacher, List<Docs> mentorTeacher) {
		return new TeacherResponseDto(
			convertToDtoList(teacher),
			convertToDtoList(majorTeacher),
			convertToDtoList(mentorTeacher)
		);
	}

	private static List<DocsNameAndEnrollResponseDto> convertToDtoList(List<Docs> docsList) {
		return docsList.stream()
			.map(DocsNameAndEnrollResponseDto::new)
			.toList();
	}
}
