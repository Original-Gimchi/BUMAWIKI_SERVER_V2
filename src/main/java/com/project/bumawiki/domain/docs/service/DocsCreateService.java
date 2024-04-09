package com.project.bumawiki.domain.docs.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.bumawiki.domain.docs.domain.Docs;
import com.project.bumawiki.domain.docs.implementation.DocsCreator;
import com.project.bumawiki.domain.docs.implementation.DocsValidator;
import com.project.bumawiki.domain.user.domain.User;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class DocsCreateService {
	private final DocsValidator docsValidator;
	private final DocsCreator docsCreator;

	public void execute(Docs docs, User user, String contents) {
		docsValidator.checkTitleAlreadyExist(docs);
		docsCreator.create(docs, user, contents);
	}
}


