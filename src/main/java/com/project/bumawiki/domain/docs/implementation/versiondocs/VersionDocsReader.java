package com.project.bumawiki.domain.docs.implementation.versiondocs;

import com.project.bumawiki.domain.docs.domain.Docs;
import com.project.bumawiki.domain.docs.domain.VersionDocs;
import com.project.bumawiki.domain.docs.domain.repository.VersionDocsRepository;
import com.project.bumawiki.global.annotation.Implementation;

import lombok.RequiredArgsConstructor;

@Implementation
@RequiredArgsConstructor
public class VersionDocsReader {
	private final VersionDocsRepository versionDocsRepository;

	public VersionDocs findLastVersion(Docs docs) {
		return versionDocsRepository.findFirstByDocsOrderByVersionDesc(docs);
	}
}
