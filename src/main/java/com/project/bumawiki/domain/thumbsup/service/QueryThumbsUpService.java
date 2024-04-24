package com.project.bumawiki.domain.thumbsup.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.bumawiki.domain.docs.domain.Docs;
import com.project.bumawiki.domain.docs.implementation.DocsValidator;
import com.project.bumawiki.domain.thumbsup.domain.repository.CustomThumbsUpRepository;
import com.project.bumawiki.domain.thumbsup.domain.repository.ThumbsUpRepository;
import com.project.bumawiki.domain.thumbsup.implementation.ThumbsUpReader;
import com.project.bumawiki.domain.user.domain.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QueryThumbsUpService {
	private final ThumbsUpReader thumbsUpReader;
	private final DocsValidator docsValidator;
	private final CustomThumbsUpRepository customThumbsUpRepository;
	private final ThumbsUpRepository thumbsUpRepository;

	public boolean checkUserLikeThisDocs(Long docsId, User currentUser) {
		docsValidator.checkDocsExist(docsId);
		return thumbsUpReader.checkDocsLike(docsId, currentUser);
	}

	public Long countThumbsUpByDocsTitle(String title) {
		return thumbsUpRepository.countByDocs_Title(title);
	}

	public List<Docs> getThumbsUp(User user) {
		return customThumbsUpRepository.getUserThumbsUp(user);
	}
}
