package com.project.bumawiki.domain.thumbsup.presentation;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.project.bumawiki.domain.auth.service.QueryAuthService;
import com.project.bumawiki.domain.thumbsup.presentation.dto.ThumbsUpRequestDto;
import com.project.bumawiki.domain.thumbsup.service.ThumbsUpManipulateService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Valid
@RestController
@RequiredArgsConstructor
@RequestMapping("api/thumbs/up")
@ResponseStatus(HttpStatus.NO_CONTENT)
public class ThumbsUpManipulateController {
	private final ThumbsUpManipulateService likesService;
	private final QueryAuthService queryAuthService;

	@PostMapping("/create")
	public void createLike(@RequestBody ThumbsUpRequestDto thumbsUpRequestDto) {
		likesService.createThumbsUp(queryAuthService.getCurrentUser(), thumbsUpRequestDto.docsId());
	}

	@DeleteMapping("/delete")
	public void removeLike(@RequestBody ThumbsUpRequestDto thumbsUpRequestDto) {
		likesService.cancelThumbsUp(queryAuthService.getCurrentUser(), thumbsUpRequestDto.docsId());
	}
}
