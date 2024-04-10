package com.project.bumawiki.domain.thumbsup.presentation;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.project.bumawiki.domain.thumbsup.service.ThumbsUpCheckService;
import com.project.bumawiki.global.util.SecurityUtil;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/docs/like")
public class ThumbsUpCheckController {
	private final ThumbsUpCheckService thumbsUpCheckService;

	@GetMapping("/{docsId}")
	@ResponseStatus(HttpStatus.OK)
	public Boolean checkYouLikeThis(@PathVariable Long docsId) {
		return thumbsUpCheckService.checkUserLikeThisDocs(
			docsId, SecurityUtil.getCurrentUserWithLogin()
		);
	}
}
