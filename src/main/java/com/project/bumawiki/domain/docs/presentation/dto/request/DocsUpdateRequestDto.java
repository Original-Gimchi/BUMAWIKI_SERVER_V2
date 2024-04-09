package com.project.bumawiki.domain.docs.presentation.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class DocsUpdateRequestDto {

	@NotNull
	private String contents;

	@NotNull(message = "업데이트할 문서의 버전이 전달되지 않았습니다.")
	private int updatingVersion;
}
