package com.project.bumawiki.domain.docs.presentation.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.bumawiki.domain.docs.domain.Docs;
import com.project.bumawiki.domain.docs.presentation.dto.response.DocsNameAndEnrollResponseDto;

public class TeacherResponseDto {
	@JsonProperty
	private List<DocsNameAndEnrollResponseDto> teacher;
	@JsonProperty
	private List<DocsNameAndEnrollResponseDto> majorTeacher;
	@JsonProperty
	private List<DocsNameAndEnrollResponseDto> mentorTeacher;

	public TeacherResponseDto(List<Docs> teacher,
		List<Docs> majorTeacher, List<Docs> mentorTeacher) {
		this.teacher = teacher.stream()
			.map(DocsNameAndEnrollResponseDto::new)
			.collect(Collectors.toList());

		this.majorTeacher = majorTeacher.stream()
			.map(DocsNameAndEnrollResponseDto::new)
			.collect(Collectors.toList());

		this.mentorTeacher = majorTeacher.stream()
			.map(DocsNameAndEnrollResponseDto::new)
			.collect(Collectors.toList());
	}
}
