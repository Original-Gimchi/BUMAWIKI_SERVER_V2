package com.project.bumawiki.domain.docs.implementation;

import com.project.bumawiki.domain.docs.domain.Docs;
import com.project.bumawiki.domain.docs.domain.VersionDocs;
import com.project.bumawiki.domain.docs.domain.repository.DocsRepository;
import com.project.bumawiki.domain.docs.domain.type.DocsType;
import com.project.bumawiki.domain.docs.domain.type.Status;
import com.project.bumawiki.domain.docs.exception.NoUpdatableDocsException;
import com.project.bumawiki.domain.user.domain.User;
import com.project.bumawiki.global.annotation.Implementation;
import com.project.bumawiki.global.error.exception.BumawikiException;
import com.project.bumawiki.global.error.exception.ErrorCode;

import lombok.RequiredArgsConstructor;

@Implementation
@RequiredArgsConstructor
public class DocsValidator {
	private final DocsRepository docsRepository;
	private final DocsReader docsReader;

	public void checkTitleAlreadyExist(Docs docs) {
		if (docsRepository.existsByTitle(docs.getTitle())) {
			throw new BumawikiException(ErrorCode.DOCS_TITLE_ALREADY_EXIST);
		}
	}

	public void checkDocsExist(Long docsId) {
		if (!docsRepository.existsById(docsId)) {
			throw new BumawikiException(ErrorCode.DOCS_NOT_FOUND);
		}
	}

	public void checkUpdateOneSelf(User user, Docs docs) {
		// 학생문서는 이름과 연도가 일치할 떄 수정이 불가함
		if (docs.getDocsType().equals(DocsType.STUDENT)) {
			if (docs.getTitle().contains(user.getName()) && docs.getEnroll().equals(user.getEnroll())) {
				throw new BumawikiException(ErrorCode.CANNOT_CHANGE_YOUR_DOCS);
			}
			return;
		}
		// 일반문서는 이름이 일치할 때 수정이 불가함
		if (docs.getTitle().contains(user.getName())) {
			throw new BumawikiException(ErrorCode.CANNOT_CHANGE_YOUR_DOCS);
		}
	}

	public void checkUpdatableDocsType(DocsType docsType) {
		if (docsType.equals(DocsType.READONLY)) {
			throw NoUpdatableDocsException.EXCEPTION;
		}
	}

	public void checkGood(Docs docs) {
		if (!docs.getStatus().equals(Status.GOOD)) {
			throw new BumawikiException(ErrorCode.DOCS_CONFLICTED);
		}
	}

	public void checkConflicted(Docs docs) {
		if (!docs.getStatus().equals(Status.CONFLICTED)) {
			throw new BumawikiException(ErrorCode.DOCS_IS_NOT_CONFLICTED);
		}
	}

	public boolean isConflict(Docs docs, Integer updatingVersion) {
		VersionDocs lastVersionDocs = docsReader.findLastVersion(docs);
		return !lastVersionDocs.getVersion().equals(updatingVersion);
	}
}
