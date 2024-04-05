package com.project.bumawiki.domain.docs.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.bumawiki.domain.docs.domain.Docs;
import com.project.bumawiki.domain.docs.domain.repository.DocsRepositoryMapper;
import com.project.bumawiki.domain.user.UserFacade;
import com.project.bumawiki.domain.user.domain.User;
import com.project.bumawiki.global.error.exception.ErrorCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DocsCheckYouLikeThisService {
	private final DocsRepositoryMapper docsRepositoryMapper;
	private final UserFacade userFacade;

	public boolean checkUserLikeThisDocs(final Long docsId) {
		User currentUser = userFacade.getCurrentUser();
		Docs docs = docsRepositoryMapper.findById(docsId, ErrorCode.DOCS_NOT_FOUND);
		User foundUser = userFacade.getUserByEmail(currentUser.getEmail());

		return docs.doesUserLike(foundUser);
	}
}
