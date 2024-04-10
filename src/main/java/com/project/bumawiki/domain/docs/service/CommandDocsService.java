package com.project.bumawiki.domain.docs.service;

import org.springframework.stereotype.Service;

import com.project.bumawiki.domain.docs.domain.Docs;
import com.project.bumawiki.domain.docs.domain.type.DocsType;
import com.project.bumawiki.domain.docs.domain.type.Status;
import com.project.bumawiki.domain.docs.implementation.DocsCreator;
import com.project.bumawiki.domain.docs.implementation.DocsDeleter;
import com.project.bumawiki.domain.docs.implementation.DocsReader;
import com.project.bumawiki.domain.docs.implementation.DocsUpdater;
import com.project.bumawiki.domain.docs.implementation.DocsValidator;
import com.project.bumawiki.domain.thumbsup.implementation.ThumbsUpDeleter;
import com.project.bumawiki.domain.user.domain.User;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class CommandDocsService {
	private final DocsCreator docsCreator;
	private final DocsReader docsReader;
	private final DocsUpdater docsUpdater;
	private final DocsDeleter docsDeleter;
	private final DocsValidator docsValidator;

	private final ThumbsUpDeleter thumbsUpDeleter;

	public void create(Docs docs, User user, String contents) {
		docsValidator.checkTitleAlreadyExist(docs);
		docsCreator.create(docs, user, contents);
	}

	public void delete(Long id) {
		docsDeleter.delete(id);
		thumbsUpDeleter.deleteAllByDocsId(id);
	}

	public void update(User user, String title, String contents, Integer updatingVersion) {
		Docs docs = docsReader.findByTitle(title);

		docsValidator.checkUpdatableDocsType(docs.getDocsType());
		docsValidator.checkUpdateOneSelf(user, docs);
		docsValidator.checkGood(docs);

		if (docsValidator.isConflict(docs, updatingVersion)) {
			docsUpdater.updateStatus(docs, Status.CONFLICTED);
		}

		docsCreator.createVersionDocs(docs, user, contents);
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
