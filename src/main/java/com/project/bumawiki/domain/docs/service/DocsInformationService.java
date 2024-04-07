package com.project.bumawiki.domain.docs.service;

import static org.bitbucket.cowwoc.diffmatchpatch.DiffMatchPatch.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.bitbucket.cowwoc.diffmatchpatch.DiffMatchPatch;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.bumawiki.domain.contribute.domain.repository.ContributeRepository;
import com.project.bumawiki.domain.docs.domain.Docs;
import com.project.bumawiki.domain.docs.domain.VersionDocs;
import com.project.bumawiki.domain.docs.domain.repository.DocsRepository;
import com.project.bumawiki.domain.docs.domain.type.DocsType;
import com.project.bumawiki.domain.docs.exception.DocsNotFoundException;
import com.project.bumawiki.domain.docs.exception.VersionNotExistException;
import com.project.bumawiki.domain.docs.presentation.dto.ClubResponseDto;
import com.project.bumawiki.domain.docs.presentation.dto.TeacherResponseDto;
import com.project.bumawiki.domain.docs.presentation.dto.VersionDocsSummaryDto;
import com.project.bumawiki.domain.docs.presentation.dto.response.DocsNameAndEnrollResponseDto;
import com.project.bumawiki.domain.docs.presentation.dto.response.DocsResponseDto;
import com.project.bumawiki.domain.docs.presentation.dto.response.DocsThumbsUpResponseDto;
import com.project.bumawiki.domain.docs.presentation.dto.response.VersionDocsDiffResponseDto;
import com.project.bumawiki.domain.docs.presentation.dto.response.VersionResponseDto;
import com.project.bumawiki.domain.user.domain.User;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class DocsInformationService {
	private final DocsRepository docsRepository;
	private final ContributeRepository contributeRepository;

	public List<DocsNameAndEnrollResponseDto> findByDocsType(final DocsType docsType) {
		List<Docs> allStudent = docsRepository.findByDocsType(docsType);

		return allStudent.stream()
			.map(DocsNameAndEnrollResponseDto::new)
			.collect(Collectors.toList());
	}

	public Map<Integer, List<DocsNameAndEnrollResponseDto>> findByDocsTypeOrderByEnroll(final DocsType docsType) {
		List<Docs> allDocs = docsRepository.findByDocsType(docsType);

		List<DocsNameAndEnrollResponseDto> docsList = allDocs.stream()
			.map(DocsNameAndEnrollResponseDto::new)
			.collect(Collectors.toList());

		// TreeMap을 사용하여 enroll 값을 기준으로 정렬된 Map을 만듭니다.
		Map<Integer, List<DocsNameAndEnrollResponseDto>> enrollMap = new TreeMap<>(Collections.reverseOrder());

		// 기존의 리스트를 순회하면서 enroll 값을 기준으로 Map에 추가합니다.
		for (DocsNameAndEnrollResponseDto doc : docsList) {
			enrollMap.computeIfAbsent(doc.getEnroll(), k -> new ArrayList<>()).add(doc);
		}

		return enrollMap;
	}

	@Transactional(readOnly = true)
	public List<DocsNameAndEnrollResponseDto> findAllByTitle(String title) {
		List<Docs> docs = docsRepository.findAllByTitle(title);

		if (docs.size() == 0) {
			throw DocsNotFoundException.EXCEPTION;
		}

		return docs.stream()
			.map(DocsNameAndEnrollResponseDto::new)
			.collect(Collectors.toList());
	}

	@Transactional
	public DocsResponseDto findDocs(String title) {
		Docs docs = docsRepository.findByTitle(title)
			.orElseThrow(() -> DocsNotFoundException.EXCEPTION);

		List<User> contributors = contributeRepository.findUserAllByDocs(docs);

		return new DocsResponseDto(docs, contributors);
	}

	public VersionResponseDto findDocsVersion(String title) {
		Docs docs = docsRepository.findByTitle(title)
			.orElseThrow(() -> DocsNotFoundException.EXCEPTION);

		return docsRepository.getDocsVersion(docs);
	}

	public List<DocsNameAndEnrollResponseDto> showDocsModifiedAtDesc(Pageable pageable) {
		return docsRepository.findByLastModifiedAt(pageable)
			.stream()
			.map(DocsNameAndEnrollResponseDto::new)
			.collect(Collectors.toList());
	}

	public List<DocsNameAndEnrollResponseDto> showDocsModifiedAtAsc(Pageable pageable) {
		return docsRepository.findByLastModifiedAtAsc(pageable)
			.stream()
			.map(DocsNameAndEnrollResponseDto::new)
			.collect(Collectors.toList());
	}

	public List<DocsNameAndEnrollResponseDto> showDocsModifiedAtDescAll() {
		return docsRepository.findByLastModifiedAtAll()
			.stream()
			.map(DocsNameAndEnrollResponseDto::new)
			.collect(Collectors.toList());
	}

	public VersionDocsDiffResponseDto showVersionDocsDiff(String title, Long version) {
		Docs docs = docsRepository.findByTitle(title).orElseThrow(
			() -> DocsNotFoundException.EXCEPTION
		);
		String baseDocs = "";
		String versionedDocs;
		List<VersionDocs> versionDocs = docs.getDocsVersion();
		try {
			versionedDocs = versionDocs.get(version.intValue()).getContents();
			if (version > 0) {
				baseDocs = versionDocs.get((int)(version - 1)).getContents();
			}
		} catch (IndexOutOfBoundsException e) {
			throw VersionNotExistException.EXCEPTION;
		}

		DiffMatchPatch dmp = new DiffMatchPatch();
		LinkedList<Diff> diff = dmp.diffMain(baseDocs, versionedDocs);
		dmp.diffCleanupSemantic(diff);

		return new VersionDocsDiffResponseDto(docs.getTitle(), docs.getDocsType(),
			new VersionDocsSummaryDto(versionDocs.get(version.intValue())), new ArrayList<>(diff));
	}

	@Transactional(readOnly = true)
	public DocsThumbsUpResponseDto getDocsThumbsUpsCount(String title) {
		Docs docs = docsRepository.findByTitle(title).orElseThrow(
			() -> DocsNotFoundException.EXCEPTION
		);

		return new DocsThumbsUpResponseDto(docs.getThumbsUpsCount());
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
}
