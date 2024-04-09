package com.project.bumawiki.domain.docs.presentation;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.bumawiki.domain.docs.presentation.dto.response.DocsPopularResponseDto;
import com.project.bumawiki.domain.docs.service.DocsPopularInformationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/docs")
@RequiredArgsConstructor
public class DocsPopularInformationController {
	private final DocsPopularInformationService docsPopularInformationService;

	@GetMapping("/popular")
	public ResponseEntity<List<DocsPopularResponseDto>> docsPopular() {
		return ResponseEntity.ok(docsPopularInformationService.getDocsByPopular());
	}
}
