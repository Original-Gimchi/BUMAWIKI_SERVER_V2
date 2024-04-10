package com.project.bumawiki.domain.docs.service;

import java.util.LinkedList;
import java.util.List;

import com.project.bumawiki.domain.docs.implementation.DocsReader;

import com.project.bumawiki.domain.docs.implementation.DocsUpdater;
import com.project.bumawiki.domain.docs.implementation.DocsValidator;
import com.project.bumawiki.domain.docs.implementation.VersionDocsReader;
import com.project.bumawiki.domain.docs.implementation.versiondocs.VersionDocsCreator;

import com.project.bumawiki.domain.docs.util.DocsUtil;
import com.project.bumawiki.global.util.SecurityUtil;

import org.bitbucket.cowwoc.diffmatchpatch.DiffMatchPatch;
import org.springframework.stereotype.Service;

import com.project.bumawiki.domain.docs.domain.Docs;
import com.project.bumawiki.domain.docs.domain.VersionDocs;
import com.project.bumawiki.domain.docs.domain.type.Status;
import com.project.bumawiki.domain.docs.presentation.dto.DocsConflictSolveDto;
import com.project.bumawiki.domain.docs.presentation.dto.MergeConflictDataResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DocsMergeConflictService {
	private final DocsReader docsReader;
	private final VersionDocsReader versionDocsReader;
	private final VersionDocsCreator versionDocsCreator;
	private final DocsValidator docsValidator;
	private final DocsUpdater docsUpdater;

	public MergeConflictDataResponse getMergeConflict(String title) {
		Docs docs = docsReader.findByTitle(title);

		docsValidator.checkConflicted(docs);

		// 버전 최신순 3가지 조회가 전체에서 자르는지 3개만 가져오는지 확인이 필요합니다
		List<VersionDocs> docsVersion = versionDocsReader.findTop3ByDocs(docs);

		String firstDocsContent = docsVersion.get(0).getContents();
		String secondDocsContent = docsVersion.get(1).getContents();
		String originalDocsContent = docsVersion.get(2).getContents();

		//최신글의 겹치는 점과 지금 바꾸려는 글의 차이점을 조회
		LinkedList<DiffMatchPatch.Diff> diff1 = DocsUtil.getDiff(originalDocsContent, firstDocsContent);
		LinkedList<DiffMatchPatch.Diff> diff2 = DocsUtil.getDiff(originalDocsContent, secondDocsContent);

		return new MergeConflictDataResponse(
			firstDocsContent,
			secondDocsContent,
			originalDocsContent,
			diff1,
			diff2
		);
	}


	public void solveConflict(String title, DocsConflictSolveDto dto) {
		Docs docs = docsReader.findByTitle(title);

		docsValidator.checkConflicted(docs);

		versionDocsCreator.create(
			docs,
			SecurityUtil.getCurrentUserWithLogin(),
			dto.contents()
		);

		docsUpdater.updateStatus(docs, Status.GOOD);
	}
}
