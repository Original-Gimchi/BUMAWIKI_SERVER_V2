package com.project.bumawiki.domain.thumbsup.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.bumawiki.domain.docs.domain.Docs;
import com.project.bumawiki.domain.docs.implementation.DocsReader;
import com.project.bumawiki.domain.thumbsup.implementation.ThumbUpDeleter;
import com.project.bumawiki.domain.thumbsup.implementation.ThumbsUpCreator;
import com.project.bumawiki.domain.thumbsup.implementation.ThumbsUpValidator;
import com.project.bumawiki.domain.user.domain.User;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ThumbsUpManipulateService {
	private final DocsReader docsReader;
	private final ThumbsUpValidator thumbsUpValidator;
	private final ThumbsUpCreator thumbsUpCreator;
	private final ThumbUpDeleter thumbUpDeleter;

	public void createThumbsUp(User user, Long docsId) {
		Docs docs = docsReader.findById(docsId);
		thumbsUpValidator.checkAlreadyThumbsUp(docs, user);

		thumbsUpCreator.create(docs, user);
	}

	public void cancelThumbsUp(User user, Long docsId) {
		Docs docs = docsReader.findById(docsId);
		thumbsUpValidator.checkNotThumbsUp(docs, user);

		thumbUpDeleter.delete(docs, user);
	}
}
