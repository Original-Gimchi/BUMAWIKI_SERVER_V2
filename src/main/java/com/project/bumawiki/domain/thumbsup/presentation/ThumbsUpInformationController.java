package com.project.bumawiki.domain.thumbsup.presentation;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.project.bumawiki.domain.thumbsup.domain.repository.CustomThumbsUpRepository;
import com.project.bumawiki.domain.thumbsup.presentation.dto.ThumbsUpResponseDto;
import com.project.bumawiki.global.util.SecurityUtil;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/thumbs/up")
public class ThumbsUpInformationController {
	private final CustomThumbsUpRepository customThumbsUpRepository;

	@GetMapping("/get")
	@ResponseStatus(HttpStatus.OK)
	public List<ThumbsUpResponseDto> getThumbsUp() {
		return customThumbsUpRepository.getUserThumbsUp(
			SecurityUtil.getCurrentUserWithLogin()
		);
	}
}
