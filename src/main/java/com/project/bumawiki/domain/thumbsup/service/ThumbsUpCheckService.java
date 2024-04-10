package com.project.bumawiki.domain.thumbsup.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.bumawiki.domain.docs.implementation.DocsValidator;
import com.project.bumawiki.domain.thumbsup.implementation.ThumbsUpReader;
import com.project.bumawiki.domain.user.domain.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ThumbsUpCheckService {
	private final ThumbsUpReader thumbsUpReader;
	private final DocsValidator docsValidator;

	public boolean checkUserLikeThisDocs(Long docsId, User currentUser) {
		docsValidator.checkDocsExist(docsId);
		return thumbsUpReader.checkDocsLike(docsId, currentUser);
	}
}
