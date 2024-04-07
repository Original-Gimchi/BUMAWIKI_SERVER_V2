package com.project.bumawiki.domain.docs.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class DocsTitleUpdateRequestDto {
	@NotBlank
	private String title;
}
