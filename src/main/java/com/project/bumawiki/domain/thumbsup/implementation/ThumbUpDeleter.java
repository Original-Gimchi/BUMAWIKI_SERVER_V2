package com.project.bumawiki.domain.thumbsup.implementation;

import com.project.bumawiki.domain.docs.domain.Docs;
import com.project.bumawiki.domain.thumbsup.domain.repository.ThumbsUpRepository;
import com.project.bumawiki.domain.user.domain.User;
import com.project.bumawiki.global.annotation.Implementation;

import lombok.RequiredArgsConstructor;

@Implementation
@RequiredArgsConstructor
public class ThumbUpDeleter {
	private final ThumbsUpRepository thumbsUpRepository;

	public void delete(Docs docs, User user) {
		thumbsUpRepository.deleteByDocsAndUser(docs, user);
	}
}
