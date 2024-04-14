package com.project.bumawiki.domain.docs.service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.bitbucket.cowwoc.diffmatchpatch.DiffMatchPatch;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.bumawiki.domain.docs.domain.Docs;
import com.project.bumawiki.domain.docs.domain.VersionDocs;
import com.project.bumawiki.domain.docs.domain.repository.DocsRepository;
import com.project.bumawiki.domain.docs.domain.repository.VersionDocsRepository;
import com.project.bumawiki.domain.docs.domain.type.DocsType;
import com.project.bumawiki.domain.docs.implementation.DocsReader;
import com.project.bumawiki.domain.docs.implementation.DocsValidator;
import com.project.bumawiki.domain.docs.presentation.dto.response.ClubResponseDto;
import com.project.bumawiki.domain.docs.presentation.dto.response.DocsResponseDto;
import com.project.bumawiki.domain.docs.presentation.dto.response.MergeConflictDataResponseDto;
import com.project.bumawiki.domain.docs.presentation.dto.response.TeacherResponseDto;
import com.project.bumawiki.domain.docs.presentation.dto.response.VersionDocsDiffResponseDto;
import com.project.bumawiki.domain.docs.presentation.dto.response.VersionDocsSummaryResponseDto;
import com.project.bumawiki.domain.docs.presentation.dto.response.VersionResponseDto;
import com.project.bumawiki.domain.docs.util.DocsUtil;
import com.project.bumawiki.domain.user.domain.User;
import com.project.bumawiki.global.error.exception.BumawikiException;
import com.project.bumawiki.global.error.exception.ErrorCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QueryDocsService {
	private final DocsRepository docsRepository;
	private final VersionDocsRepository versionDocsRepository;
	private final DocsReader docsReader;
	private final DocsValidator docsValidator;

	public List<Docs> findAllByTitle(String title) {
		List<Docs> docs = docsRepository.findAllByTitle(title);

		if (docs.isEmpty()) {
			throw new BumawikiException(ErrorCode.DOCS_NOT_FOUND);
		}

		return docs;
	}

	public DocsResponseDto findDocs(String title) {
		Docs docs = docsRepository.getByTitle(title);

		List<User> contributors = versionDocsRepository.findByDocs(docs)
			.stream()
			.map(VersionDocs::getUser)
			.toList();

		return new DocsResponseDto(docs, contributors, docsReader.findLastVersion(docs));
	}

	public VersionResponseDto findDocsVersion(String title) {
		Docs docs = docsRepository.getByTitle(title);
		return docsRepository.getDocsVersion(docs);
	}

	public List<Docs> showDocsModifiedAtDesc(Pageable pageable) {
		return docsRepository.findByLastModifiedAt(pageable);
	}

	public List<Docs> showDocsModifiedAtDescAll() {
		return docsRepository.findByLastModifiedAtAll();
	}

	public VersionDocsDiffResponseDto showVersionDocsDiff(String title, Integer version) {
		Docs docs = docsRepository.getByTitle(title);
		VersionDocs targetVersionDocs = versionDocsRepository.findByDocsAndVersion(docs, version)
			.orElseThrow(() -> new BumawikiException(ErrorCode.VERSION_NOT_EXIST));

		Optional<VersionDocs> baseVersionDocs = versionDocsRepository.findByDocsAndVersion(docs, version - 1);

		LinkedList<DiffMatchPatch.Diff> diff = getDiff(baseVersionDocs, targetVersionDocs);

		return new VersionDocsDiffResponseDto(docs.getTitle(), docs.getDocsType(),
			new VersionDocsSummaryResponseDto(targetVersionDocs), new ArrayList<>(diff));
	}

	private LinkedList<DiffMatchPatch.Diff> getDiff(Optional<VersionDocs> baseVersionDocs,
		VersionDocs targetVersionDocs) {
		LinkedList<DiffMatchPatch.Diff> diff;
		if (baseVersionDocs.isEmpty()) {
			diff = DocsUtil.getDiff("", targetVersionDocs.getContents());
		} else {
			diff = DocsUtil.getDiff(baseVersionDocs.get().getContents(), targetVersionDocs.getContents());
		}
		return diff;
	}

	//Docs Type으로 조회
	public List<Docs> findByDocsTypeOrderByEnroll(DocsType docsType) {
		return docsRepository.findByDocsType(docsType);
	}

	public TeacherResponseDto getAllTeacher() {
		return TeacherResponseDto.from(
			findByDocsType(DocsType.TEACHER),
			findByDocsType(DocsType.MAJOR_TEACHER),
			findByDocsType(DocsType.MENTOR_TEACHER)
		);
	}

	public ClubResponseDto getAllClub() {
		return ClubResponseDto.from(
			findByDocsType(DocsType.CLUB),
			findByDocsType(DocsType.FREE_CLUB)
		);
	}

	private List<Docs> findByDocsType(DocsType docsType) {
		return docsRepository.findByDocsType(docsType);
	}

	public List<VersionDocs> findAllVersionDocsByUser(User user) {
		return versionDocsRepository.findAllByUser(user);
	}

	public MergeConflictDataResponseDto getMergeConflict(String title) {
		Docs docs = docsReader.findByTitle(title);

		docsValidator.checkConflicted(docs);

		// 버전 최신순 3가지 조회가 전체에서 자르는지 3개만 가져오는지 확인이 필요합니다
		List<VersionDocs> docsVersion = docsReader.findTop3ByDocs(docs);

		String firstDocsContent = docsVersion.get(0).getContents();
		String secondDocsContent = docsVersion.get(1).getContents();
		String originalDocsContent = docsVersion.get(2).getContents();

		//최신글의 겹치는 점과 지금 바꾸려는 글의 차이점을 조회
		LinkedList<DiffMatchPatch.Diff> diff1 = DocsUtil.getDiff(originalDocsContent, firstDocsContent);
		LinkedList<DiffMatchPatch.Diff> diff2 = DocsUtil.getDiff(originalDocsContent, secondDocsContent);

		return new MergeConflictDataResponseDto(
			firstDocsContent,
			secondDocsContent,
			originalDocsContent,
			diff1,
			diff2
		);
	}
}
