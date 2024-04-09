package com.project.bumawiki.domain.thumbsup.presentation;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.bumawiki.domain.thumbsup.presentation.dto.ThumbsUpResponseDto;
import com.project.bumawiki.domain.thumbsup.service.ThumbsUpInformationService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/api/thumbs/up")
@RestController
public class ThumbsUpInformationController {
	private final ThumbsUpInformationService thumbsUpInformationService;

	@GetMapping("/get")
	public ResponseEntity<List<ThumbsUpResponseDto>> getThumbsUp() {
		return ResponseEntity.ok(thumbsUpInformationService.getThumbsUpList());
	}
}
