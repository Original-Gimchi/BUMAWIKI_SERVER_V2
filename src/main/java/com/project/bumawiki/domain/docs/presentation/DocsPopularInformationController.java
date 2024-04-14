package com.project.bumawiki.domain.docs.presentation;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.project.bumawiki.domain.docs.presentation.dto.response.DocsPopularResponseDto;
import com.project.bumawiki.domain.docs.service.QueryDocsService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/docs")
public class DocsPopularInformationController {
	private final QueryDocsService queryDocsService;

	@GetMapping("/popular")
	@ResponseStatus(HttpStatus.OK)
	public List<DocsPopularResponseDto> docsPopular() {
		return queryDocsService.readByThumbsUpsDesc();
	}
}
