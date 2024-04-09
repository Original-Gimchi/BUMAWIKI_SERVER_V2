package com.project.bumawiki.domain.docs.presentation.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.bumawiki.domain.docs.presentation.dto.response.DocsNameAndEnrollResponseDto;

public class TeacherResponseDto {
	@JsonProperty
	private List<DocsNameAndEnrollResponseDto> teacher;
	@JsonProperty
	private List<DocsNameAndEnrollResponseDto> majorTeacher;
	@JsonProperty
	private List<DocsNameAndEnrollResponseDto> mentorTeacher;

	public TeacherResponseDto(List<DocsNameAndEnrollResponseDto> teacher,
		List<DocsNameAndEnrollResponseDto> majorTeacher, List<DocsNameAndEnrollResponseDto> mentorTeacher) {
		this.teacher = teacher;
		this.majorTeacher = majorTeacher;
		this.mentorTeacher = mentorTeacher;
	}
}
