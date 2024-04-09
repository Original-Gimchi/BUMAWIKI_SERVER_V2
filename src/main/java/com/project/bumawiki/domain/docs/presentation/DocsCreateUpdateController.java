package com.project.bumawiki.domain.docs.presentation;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.bumawiki.domain.docs.presentation.dto.request.DocsCreateRequestDto;
import com.project.bumawiki.domain.docs.presentation.dto.request.DocsTitleUpdateRequestDto;
import com.project.bumawiki.domain.docs.presentation.dto.request.DocsTypeUpdateDto;
import com.project.bumawiki.domain.docs.presentation.dto.request.DocsUpdateRequestDto;
import com.project.bumawiki.domain.docs.service.DocsCreateService;
import com.project.bumawiki.domain.docs.service.DocsUpdateService;

import lombok.RequiredArgsConstructor;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/docs")
public class DocsCreateUpdateController {

	private final DocsUpdateService docsUpdateService;
	private final DocsCreateService docsCreateService;

	@PostMapping("/create")
	public ResponseEntity<Long> createDocs(@RequestBody DocsCreateRequestDto request) throws IOException {
		return ResponseEntity.ok(docsCreateService.execute(request));
	}

	@PutMapping("/update/{title}")
	public ResponseEntity<Long> updateDocs(@RequestHeader("Authorization") String bearer, @PathVariable String title,
		@RequestBody DocsUpdateRequestDto request) throws IOException {
		return ResponseEntity.ok(docsUpdateService.execute(bearer, title, request));
	}

	@PutMapping("/update/title/{title}")
	public ResponseEntity<Long> updateDocsTitle(@RequestBody DocsTitleUpdateRequestDto requestDto,
		@PathVariable String title) {
		return ResponseEntity.ok(docsUpdateService.titleUpdate(title, requestDto));
	}

	@PutMapping("/update/docsType")
	public ResponseEntity<Long> updateDocsType(@RequestBody DocsTypeUpdateDto requestDto) {
		return ResponseEntity.ok(docsUpdateService.docsTypeUpdate(requestDto));
	}
}
