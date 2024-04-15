package com.project.bumawiki.domain.docs.presentation;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.project.bumawiki.domain.auth.annotation.LoginRequired;
import com.project.bumawiki.domain.auth.service.QueryAuthService;
import com.project.bumawiki.domain.docs.presentation.dto.request.DocsConflictSolveRequestDto;
import com.project.bumawiki.domain.docs.presentation.dto.request.DocsCreateRequestDto;
import com.project.bumawiki.domain.docs.presentation.dto.request.DocsTitleUpdateRequestDto;
import com.project.bumawiki.domain.docs.presentation.dto.request.DocsTypeUpdateRequestDto;
import com.project.bumawiki.domain.docs.presentation.dto.request.DocsUpdateRequestDto;
import com.project.bumawiki.domain.docs.service.CommandDocsService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/docs")
@ResponseStatus(HttpStatus.NO_CONTENT)
public class CommandDocsController {
	private final CommandDocsService commandDocsService;
	private final QueryAuthService queryAuthService;

	@PostMapping
	@LoginRequired
	public void createDocs(@RequestBody DocsCreateRequestDto request) {
		commandDocsService.create(request.toEntity(), queryAuthService.getCurrentUser(), request.contents());
	}

	@PutMapping("/{title}")
	public void updateDocs(@PathVariable String title,
		@RequestBody DocsUpdateRequestDto request) {
		commandDocsService.update(
			queryAuthService.getCurrentUser(),
			title,
			request.contents(),
			request.updatingVersion());
	}

	@PutMapping("/title/{title}")
	public void updateDocsTitle(@PathVariable String title,
		@RequestBody DocsTitleUpdateRequestDto request) {
		commandDocsService.titleUpdate(title, request.title());
	}

	@PutMapping("/docsType")
	public void updateDocsType(@RequestBody DocsTypeUpdateRequestDto requestDto) {
		commandDocsService.docsTypeUpdate(requestDto.id(), requestDto.docsType());
	}

	@DeleteMapping("{id}")
	public void deleteDocs(@PathVariable Long id) {
		commandDocsService.delete(id);
	}

	@PutMapping("/merge/{title}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@LoginRequired
	public void solveConflict(@PathVariable String title, @RequestBody DocsConflictSolveRequestDto dto) {
		commandDocsService.solveConflict(title, dto.contents(), dto.version(), queryAuthService.getCurrentUser());
	}
}
