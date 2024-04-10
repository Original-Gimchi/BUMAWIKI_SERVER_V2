package com.project.bumawiki.domain.docs.presentation;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.project.bumawiki.domain.docs.presentation.dto.request.DocsCreateRequestDto;
import com.project.bumawiki.domain.docs.presentation.dto.request.DocsTitleUpdateRequestDto;
import com.project.bumawiki.domain.docs.presentation.dto.request.DocsTypeUpdateDto;
import com.project.bumawiki.domain.docs.presentation.dto.request.DocsUpdateRequestDto;
import com.project.bumawiki.domain.docs.service.DocsCreateService;
import com.project.bumawiki.domain.docs.service.DocsUpdateService;
import com.project.bumawiki.global.util.SecurityUtil;

import lombok.RequiredArgsConstructor;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/docs")
@ResponseStatus(HttpStatus.NO_CONTENT)
public class DocsCreateUpdateController {

	private final DocsUpdateService docsUpdateService;
	private final DocsCreateService docsCreateService;

	@PostMapping("/create")
	public void createDocs(@RequestBody DocsCreateRequestDto request) {
		docsCreateService.execute(request.toEntity(), SecurityUtil.getCurrentUserWithLogin(), request.contents());
	}

	@PutMapping("/update/{title}")
	public void updateDocs(@PathVariable String title,
		@RequestBody DocsUpdateRequestDto request) {
		docsUpdateService.execute(
			SecurityUtil.getCurrentUserWithLogin(),
			title,
			request.contents(),
			request.updatingVersion());
	}

	@PutMapping("/update/title/{title}")
	public void updateDocsTitle(@PathVariable String title,
		@RequestBody DocsTitleUpdateRequestDto request) {
		docsUpdateService.titleUpdate(title, request.title());
	}

	@PutMapping("/update/docsType")
	public void updateDocsType(@RequestBody DocsTypeUpdateDto requestDto) {
		docsUpdateService.docsTypeUpdate(requestDto.id(), requestDto.docsType());
	}
}
