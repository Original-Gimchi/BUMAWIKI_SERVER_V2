package com.project.bumawiki.domain.docs.implementation.versiondocs;

import com.project.bumawiki.domain.docs.domain.Docs;
import com.project.bumawiki.domain.docs.domain.VersionDocs;
import com.project.bumawiki.domain.docs.domain.repository.VersionDocsRepository;
import com.project.bumawiki.domain.docs.implementation.DocsUpdater;
import com.project.bumawiki.domain.user.domain.User;
import com.project.bumawiki.global.annotation.Implementation;

import lombok.RequiredArgsConstructor;

@Implementation
@RequiredArgsConstructor
public class VersionDocsCreator {
	private final VersionDocsReader versionDocsReader;
	private final DocsUpdater docsUpdater;
	private final VersionDocsRepository versionDocsRepository;

	public void create(Docs docs, User user, String contents) {
		Integer lastVersion = versionDocsReader.findLastVersion(docs).getVersion();
		VersionDocs versionDocs = versionDocsRepository.save(
			new VersionDocs(
				lastVersion + 1,
				docs,
				contents,
				user
			)
		);
		docsUpdater.updateModifiedAt(docs, versionDocs.getThisVersionCreatedAt());
	}
}
