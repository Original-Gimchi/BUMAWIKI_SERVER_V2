package com.project.bumawiki.domain.docs.service;

import java.util.LinkedList;
import java.util.List;

import org.bitbucket.cowwoc.diffmatchpatch.DiffMatchPatch;
import org.springframework.stereotype.Service;

import com.project.bumawiki.domain.contribute.domain.Contribute;
import com.project.bumawiki.domain.contribute.service.ContributeService;
import com.project.bumawiki.domain.docs.domain.Docs;
import com.project.bumawiki.domain.docs.domain.VersionDocs;
import com.project.bumawiki.domain.docs.domain.repository.DocsRepository;
import com.project.bumawiki.domain.docs.domain.repository.VersionDocsRepository;
import com.project.bumawiki.domain.docs.exception.DocsNotFoundException;
import com.project.bumawiki.domain.docs.presentation.dto.DocsConflictSolveDto;
import com.project.bumawiki.domain.docs.presentation.dto.MergeConflictDataResponse;
import com.project.bumawiki.domain.docs.presentation.dto.response.ContentVersionDocsResponseDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DocsMergeConflictService {
	private final DocsRepository docsRepository;
	private final VersionDocsRepository versionDocsRepository;
	private final ContributeService contributeService;

	public MergeConflictDataResponse getMergeConflict(String title, String contents) {
		Docs docs = docsRepository.findByTitle(title)
			.orElseThrow(() -> DocsNotFoundException.EXCEPTION);

		List<VersionDocs> docsVersion = docs.getDocsVersion();

		VersionDocs firstDocs = docsVersion.get(docsVersion.size() - 2);
		VersionDocs originalDocs = docsVersion.get(docsVersion.size() - 3);

		//최신글의 겹치는 점과 지금 바꾸려는 글의 차이점을 조회
		DiffMatchPatch dmp = new DiffMatchPatch();
		LinkedList<DiffMatchPatch.Diff> diff1 = dmp.diffMain(originalDocs.getContents(), firstDocs.getContents());
		dmp.diffCleanupSemantic(diff1);

		LinkedList<DiffMatchPatch.Diff> diff2 = dmp.diffMain(originalDocs.getContents(), contents);
		dmp.diffCleanupSemantic(diff2);

		return new MergeConflictDataResponse(
			new ContentVersionDocsResponseDto(firstDocs),
			contents,
			new ContentVersionDocsResponseDto(originalDocs),
			diff1,
			diff2
		);
	}

	public void solveConflict(String title, DocsConflictSolveDto dto) {
		Docs docs = docsRepository.findByTitle(title)
			.orElseThrow(() -> DocsNotFoundException.EXCEPTION);

		VersionDocs savedVersionDocs = versionDocsRepository.save(
			VersionDocs.builder()
				.docsId(docs.getId())
				.contents(dto.contents())
				.version(docs.getLastVersion() + 1)
				.build()
		);

		Contribute contribute = contributeService.updateContribute(savedVersionDocs);
		savedVersionDocs.updateContributor(contribute);
	}
}
