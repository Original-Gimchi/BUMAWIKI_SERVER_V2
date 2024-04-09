package com.project.bumawiki.domain.docs.implementation;

import java.util.Optional;

import com.project.bumawiki.domain.docs.domain.Docs;
import com.project.bumawiki.domain.docs.domain.repository.DocsRepository;
import com.project.bumawiki.domain.docs.exception.DocsTitleAlreadyExistException;
import com.project.bumawiki.global.annotation.Implementation;

import lombok.RequiredArgsConstructor;

@Implementation
@RequiredArgsConstructor
public class DocsValidator {
	private final DocsRepository docsRepository;

	public void checkTitleAlreadyExist(String title) {
		Optional<Docs> byTitle = docsRepository.findByTitle(title);
		if (byTitle.isPresent()) {
			throw DocsTitleAlreadyExistException.EXCEPTION;
		}
	}

}
