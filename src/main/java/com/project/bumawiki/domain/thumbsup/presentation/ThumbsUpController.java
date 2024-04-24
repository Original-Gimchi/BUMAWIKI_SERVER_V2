package com.project.bumawiki.domain.thumbsup.presentation;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.project.bumawiki.domain.auth.annotation.LoginOrNot;
import com.project.bumawiki.domain.auth.annotation.LoginRequired;
import com.project.bumawiki.domain.auth.service.QueryAuthService;
import com.project.bumawiki.domain.thumbsup.presentation.dto.ThumbsUpCountResponseDto;
import com.project.bumawiki.domain.thumbsup.presentation.dto.ThumbsUpRequestDto;
import com.project.bumawiki.domain.thumbsup.presentation.dto.ThumbsUpResponseDto;
import com.project.bumawiki.domain.thumbsup.service.CommandThumbsUpService;
import com.project.bumawiki.domain.thumbsup.service.QueryThumbsUpService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Valid
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/docs")
public class ThumbsUpController {
	private final QueryThumbsUpService queryThumbsUpService;
	private final CommandThumbsUpService commandThumbsUpService;
	private final QueryAuthService queryAuthService;

	@LoginRequired
	@PostMapping("/create")
	public void createLike(@RequestBody ThumbsUpRequestDto thumbsUpRequestDto) {
		commandThumbsUpService.createThumbsUp(queryAuthService.getCurrentUser(), thumbsUpRequestDto.docsId());
	}

	@LoginOrNot
	@GetMapping("/like/{docsId}")
	@ResponseStatus(HttpStatus.OK)
	public Boolean checkYouLikeThis(@PathVariable Long docsId) {
		return queryThumbsUpService.checkUserLikeThisDocs(
			docsId, queryAuthService.getNullableCurrentUser()
		);
	}

	@LoginRequired
	@GetMapping("/my")
	public List<ThumbsUpResponseDto> getThumbsUps() {
		return queryThumbsUpService.getThumbsUp(queryAuthService.getCurrentUser());
	}

	@GetMapping("/{title}")
	public ThumbsUpCountResponseDto getThumbsUpCount(@PathVariable String title) {
		return new ThumbsUpCountResponseDto(
			queryThumbsUpService.countThumbsUpByDocsTitle(title)
		);
	}

	@LoginRequired
	@DeleteMapping
	public void cancel(@RequestBody ThumbsUpRequestDto thumbsUpRequestDto) {
		commandThumbsUpService.cancelThumbsUp(queryAuthService.getCurrentUser(), thumbsUpRequestDto.docsId());
	}
}
