package com.project.bumawiki.domain.docs.presentation;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.project.bumawiki.domain.docs.domain.repository.DocsRepository;
import com.project.bumawiki.domain.docs.presentation.dto.response.DocsPopularResponseDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/docs")
public class DocsPopularInformationController {
	private final DocsRepository docsRepository;

	@GetMapping("/popular")
	@ResponseStatus(HttpStatus.OK)
	public List<DocsPopularResponseDto> docsPopular() {
		return docsRepository.findByThumbsUpsDesc();
	}
}
