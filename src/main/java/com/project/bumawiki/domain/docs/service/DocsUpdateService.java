package com.project.bumawiki.domain.docs.service;

import org.springframework.transaction.annotation.Transactional;

import com.project.bumawiki.domain.docs.domain.Docs;
import com.project.bumawiki.domain.docs.domain.type.DocsType;
import com.project.bumawiki.domain.docs.domain.type.Status;
import com.project.bumawiki.domain.docs.implementation.DocsReader;
import com.project.bumawiki.domain.docs.implementation.DocsUpdater;
import com.project.bumawiki.domain.docs.implementation.DocsValidator;
import com.project.bumawiki.domain.docs.implementation.versiondocs.VersionDocsCreator;
import com.project.bumawiki.domain.docs.implementation.versiondocs.VersionDocsValidator;
import com.project.bumawiki.domain.user.domain.User;
import com.project.bumawiki.global.annotation.Implementation;

import lombok.RequiredArgsConstructor;

@Transactional
@Implementation
@RequiredArgsConstructor
public class DocsUpdateService {
	private final DocsReader docsReader;
	private final DocsUpdater docsUpdater;
	private final DocsValidator docsValidator;
	private final VersionDocsCreator versionDocsCreator;
	private final VersionDocsValidator versionDocsValidator;

	public void execute(User user, String title, String contents, Integer updatingVersion) {
		Docs docs = docsReader.findByTitle(title);

		docsValidator.checkUpdatableDocsType(docs.getDocsType());
		docsValidator.checkUpdateOneSelf(user, docs);
		docsValidator.checkGood(docs);

		if (versionDocsValidator.isConflict(docs, updatingVersion)) {
			docsUpdater.updateStatus(docs, Status.CONFLICTED);
		}

		versionDocsCreator.create(docs, user, contents);
	}

	public void titleUpdate(String title, String changedTitle) {
		Docs docs = docsReader.findByTitle(title);
		docsValidator.checkTitleAlreadyExist(docs);

		docsUpdater.updateTitle(docs, changedTitle);
	}

	public void docsTypeUpdate(Long id, DocsType docsType) {
		Docs docs = docsReader.findById(id);

		docsUpdater.updateType(docs, docsType);
	}

}

