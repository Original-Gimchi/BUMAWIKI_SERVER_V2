package com.project.bumawiki.domain.thumbsup.presentation;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.project.bumawiki.domain.auth.annotation.LoginRequired;
import com.project.bumawiki.domain.auth.service.QueryAuthService;
import com.project.bumawiki.domain.thumbsup.domain.repository.CustomThumbsUpRepository;
import com.project.bumawiki.domain.thumbsup.presentation.dto.DocsThumbsUpResponseDto;
import com.project.bumawiki.domain.thumbsup.presentation.dto.ThumbsUpResponseDto;
import com.project.bumawiki.domain.thumbsup.service.ThumbsUpCountService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@ResponseStatus(HttpStatus.OK)
@RequestMapping("/api/thumbs/up")
public class ThumbsUpInformationController {
	private final CustomThumbsUpRepository customThumbsUpRepository;
	private final ThumbsUpCountService thumbsUpCountService;
	private final QueryAuthService queryAuthService;

	@GetMapping("/get")
	@LoginRequired
	public List<ThumbsUpResponseDto> getThumbsUp() {
		return customThumbsUpRepository.getUserThumbsUp(
			queryAuthService.getCurrentUser()
		);
	}

	@GetMapping("/get/{title}")
	public DocsThumbsUpResponseDto getThumbsUpCount(@PathVariable String title) {
		return new DocsThumbsUpResponseDto(
			thumbsUpCountService.countThumbsUpByDocsTitle(title)
		);
	}
}
