package com.project.bumawiki.domain.docs.implementation.versiondocs;

import com.project.bumawiki.domain.docs.domain.Docs;
import com.project.bumawiki.domain.docs.domain.VersionDocs;
import com.project.bumawiki.global.annotation.Implementation;

import lombok.RequiredArgsConstructor;

@Implementation
@RequiredArgsConstructor
public class VersionDocsValidator {
	private final VersionDocsReader versionDocsReader;

	public boolean isConflict(Docs docs, Integer updatingVersion) {
		VersionDocs lastVersionDocs = versionDocsReader.findLastVersion(docs);
		return !lastVersionDocs.getVersion().equals(updatingVersion);
	}
}
