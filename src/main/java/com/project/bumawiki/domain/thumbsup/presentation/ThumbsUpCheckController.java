package com.project.bumawiki.domain.thumbsup.presentation;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.project.bumawiki.domain.auth.annotation.LoginOrNot;
import com.project.bumawiki.domain.auth.service.QueryAuthService;
import com.project.bumawiki.domain.thumbsup.service.ThumbsUpCheckService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/docs/like")
public class ThumbsUpCheckController {
	private final ThumbsUpCheckService thumbsUpCheckService;
	private final QueryAuthService queryAuthService;

	@GetMapping("/{docsId}")
	@ResponseStatus(HttpStatus.OK)
	@LoginOrNot
	public Boolean checkYouLikeThis(@PathVariable Long docsId) {
		return thumbsUpCheckService.checkUserLikeThisDocs(
			docsId, queryAuthService.getCurrentUser()
		);
	}
}
