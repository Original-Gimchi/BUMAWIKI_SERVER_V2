package com.project.bumawiki.domain.docs.presentation.dto.request;

import com.project.bumawiki.domain.docs.domain.type.DocsType;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class DocsTypeUpdateDto {

	@NotBlank
	Long id;
	@NotBlank
	DocsType docsType;

}
