package com.project.bumawiki.domain.docs.service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import com.project.bumawiki.domain.docs.implementation.DocsReader;

import org.bitbucket.cowwoc.diffmatchpatch.DiffMatchPatch;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.bumawiki.domain.docs.domain.Docs;
import com.project.bumawiki.domain.docs.domain.VersionDocs;
import com.project.bumawiki.domain.docs.domain.repository.DocsRepository;
import com.project.bumawiki.domain.docs.domain.repository.VersionDocsRepository;
import com.project.bumawiki.domain.docs.domain.type.DocsType;
import com.project.bumawiki.domain.docs.presentation.dto.ClubResponseDto;
import com.project.bumawiki.domain.docs.presentation.dto.TeacherResponseDto;
import com.project.bumawiki.domain.docs.presentation.dto.VersionDocsSummaryDto;
import com.project.bumawiki.domain.docs.presentation.dto.response.DocsResponseDto;
import com.project.bumawiki.domain.docs.presentation.dto.response.VersionDocsDiffResponseDto;
import com.project.bumawiki.domain.docs.presentation.dto.response.VersionResponseDto;
import com.project.bumawiki.domain.docs.util.DocsUtil;
import com.project.bumawiki.domain.user.domain.User;
import com.project.bumawiki.global.error.exception.BumawikiException;
import com.project.bumawiki.global.error.exception.ErrorCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DocsInformationService {
	private final DocsRepository docsRepository;
	private final VersionDocsRepository versionDocsRepository;
	private final DocsReader docsReader;

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
			new VersionDocsSummaryDto(targetVersionDocs), new ArrayList<>(diff));
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
		return new TeacherResponseDto(
			findByDocsType(DocsType.TEACHER),
			findByDocsType(DocsType.MAJOR_TEACHER),
			findByDocsType(DocsType.MENTOR_TEACHER)
		);
	}

	public ClubResponseDto getAllClub() {
		return new ClubResponseDto(
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
}
