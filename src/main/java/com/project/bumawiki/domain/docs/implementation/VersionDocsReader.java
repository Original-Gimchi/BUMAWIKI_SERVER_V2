package com.project.bumawiki.domain.docs.implementation;

import com.project.bumawiki.domain.docs.domain.Docs;
import com.project.bumawiki.domain.docs.domain.VersionDocs;
import com.project.bumawiki.domain.docs.domain.repository.VersionDocsRepository;
import com.project.bumawiki.global.annotation.Implementation;

import lombok.RequiredArgsConstructor;

import java.util.List;

@Implementation
@RequiredArgsConstructor
public class VersionDocsReader {
	private final VersionDocsRepository versionDocsRepository;

	public List<VersionDocs> findTop3ByDocs(Docs docs) {
		return versionDocsRepository.findTop3ByDocsOrderByVersion(docs);
	}
}
