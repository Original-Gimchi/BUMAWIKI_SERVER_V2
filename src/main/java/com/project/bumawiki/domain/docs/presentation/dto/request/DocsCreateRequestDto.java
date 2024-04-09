package com.project.bumawiki.domain.docs.presentation.dto.request;

import com.project.bumawiki.domain.docs.domain.Docs;
import com.project.bumawiki.domain.docs.domain.type.DocsType;

import jakarta.validation.constraints.NotBlank;

public record DocsCreateRequestDto(
	@NotBlank
	String title,
	@NotBlank
	Integer enroll,
	@NotBlank
	String contents,
	@NotBlank
	DocsType docsType
) {

	public Docs toEntity() {
		return new Docs(
			title,
			enroll,
			docsType
		);
	}
}
