package com.project.bumawiki.domain.docs.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record DocsUpdateRequestDto(
	@NotBlank(message = "업데이트할 문서의 내용이 전달되지 않았습니다.")
	String contents,

	@Positive(message = "업데이트할 문서는 양수여야합니다")
	@NotNull(message = "업데이트할 문서의 버전이 전달되지 않았습니다.")
	Integer updatingVersion
) {
}
