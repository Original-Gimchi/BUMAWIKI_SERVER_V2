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
}
