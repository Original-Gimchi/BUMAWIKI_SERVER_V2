package com.project.bumawiki.domain.docs.implementation;

import com.project.bumawiki.domain.docs.domain.Docs;
import com.project.bumawiki.domain.docs.domain.VersionDocs;
import com.project.bumawiki.domain.docs.domain.repository.DocsRepository;
import com.project.bumawiki.domain.docs.domain.repository.VersionDocsRepository;
import com.project.bumawiki.domain.user.domain.User;
import com.project.bumawiki.global.annotation.Implementation;

import lombok.RequiredArgsConstructor;

@Implementation
@RequiredArgsConstructor
public class DocsCreator {
	private final DocsRepository docsRepository;
	private final VersionDocsRepository versionDocsRepository;
	private final DocsUpdater docsUpdater;
	private final DocsReader docsReader;

	public void create(Docs docs, User user, String contents) {
		docsRepository.save(docs);
		versionDocsRepository.save(
			new VersionDocs(
				0,
				docs,
				contents,
				user
			)
		);
	}

	public void createVersionDocs(Docs docs, User user, String contents) {
		Integer lastVersion = docsReader.findLastVersion(docs).getVersion();
		VersionDocs versionDocs = versionDocsRepository.save(
			new VersionDocs(
				lastVersion + 1,
				docs,
				contents,
				user
			)
		);
		docsUpdater.updateModifiedAt(docs, versionDocs.getCreatedAt());
	}
}
