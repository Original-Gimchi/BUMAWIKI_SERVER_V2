package com.project.bumawiki.domain.docs.presentation;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.project.bumawiki.domain.docs.presentation.dto.request.DocsConflictSolveRequestDto;
import com.project.bumawiki.domain.docs.presentation.dto.response.MergeConflictDataResponseDto;
import com.project.bumawiki.domain.docs.service.DocsMergeConflictService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/docs/merge")
public class DocsMergeConflictController {

	private final DocsMergeConflictService mergeConflictService;

	@GetMapping("/{title}")
	@ResponseStatus(HttpStatus.OK)
	public MergeConflictDataResponseDto getMergeConflictData(@PathVariable String title) {
		return mergeConflictService.getMergeConflict(title);
	}

	@PutMapping("/{title}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void solveConflict(@PathVariable String title, @RequestBody DocsConflictSolveRequestDto dto) {
		mergeConflictService.solveConflict(title, dto);
	}
}
