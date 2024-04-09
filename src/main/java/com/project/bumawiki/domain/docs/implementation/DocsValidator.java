package com.project.bumawiki.domain.docs.implementation;

import com.project.bumawiki.domain.docs.domain.Docs;
import com.project.bumawiki.domain.docs.domain.repository.DocsRepository;
import com.project.bumawiki.global.annotation.Implementation;
import com.project.bumawiki.global.error.exception.BumawikiException;
import com.project.bumawiki.global.error.exception.ErrorCode;

import lombok.RequiredArgsConstructor;

@Implementation
@RequiredArgsConstructor
public class DocsValidator {
	private final DocsRepository docsRepository;

	public void checkTitleAlreadyExist(Docs docs) {
		if (docsRepository.existsByTitle(docs.getTitle())) {
			throw new BumawikiException(ErrorCode.DOCS_TITLE_ALREADY_EXIST);
		}
	}

	public void checkDocsExist(Long docsId) {
		if (!docsRepository.existsById(docsId)) {
			throw new BumawikiException(ErrorCode.DOCS_NOT_FOUND);
		}
	}
}
