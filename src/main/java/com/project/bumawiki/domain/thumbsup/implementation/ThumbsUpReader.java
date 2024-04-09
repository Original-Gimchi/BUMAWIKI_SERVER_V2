package com.project.bumawiki.domain.thumbsup.implementation;

import com.project.bumawiki.domain.thumbsup.domain.repository.ThumbsUpRepository;
import com.project.bumawiki.domain.user.domain.User;
import com.project.bumawiki.global.annotation.Implementation;

import lombok.RequiredArgsConstructor;

@Implementation
@RequiredArgsConstructor
public class ThumbsUpReader {

	private final ThumbsUpRepository thumbsUpRepository;

	public Boolean checkDocsLike(Long docsId, User user) {
		return thumbsUpRepository.existsByDocs_IdAndUser(docsId, user);
	}
}
