package com.project.bumawiki.domain.docs.implementation;

import java.util.List;

import com.project.bumawiki.domain.docs.domain.Docs;
import com.project.bumawiki.domain.docs.domain.VersionDocs;
import com.project.bumawiki.domain.docs.domain.repository.DocsRepository;
import com.project.bumawiki.domain.docs.domain.repository.VersionDocsRepository;
import com.project.bumawiki.global.annotation.Implementation;
import com.project.bumawiki.global.error.exception.BumawikiException;
import com.project.bumawiki.global.error.exception.ErrorCode;

import lombok.RequiredArgsConstructor;

@Implementation
@RequiredArgsConstructor
public class DocsReader {

	private final DocsRepository docsRepository;
	private final VersionDocsRepository versionDocsRepository;

	public Docs findById(Long docsId) {
		return docsRepository.findById(docsId)
			.orElseThrow(() -> new BumawikiException(ErrorCode.DOCS_NOT_FOUND));
	}

	public Docs findByTitle(String title) {
		return docsRepository.findByTitle(title)
			.orElseThrow(() -> new BumawikiException(ErrorCode.DOCS_NOT_FOUND));
	}

	public List<VersionDocs> findTop3ByDocs(Docs docs) {
		return versionDocsRepository.findTop3ByDocsOrderByVersion(docs);
	}

	public VersionDocs findLastVersion(Docs docs) {
		return versionDocsRepository.findFirstByDocsOrderByVersionDesc(docs);
	}
}
