package com.project.bumawiki.domain.docs.domain.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.project.bumawiki.domain.docs.domain.Docs;
import com.project.bumawiki.domain.docs.exception.DocsTitleAlreadyExistException;
import com.project.bumawiki.domain.docs.presentation.dto.request.DocsCreateRequestDto;
import com.project.bumawiki.global.error.exception.BumawikiException;
import com.project.bumawiki.global.error.exception.ErrorCode;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class DocsRepositoryMapper {
	private final DocsRepository docsRepository;

	public Docs findById(Long id, ErrorCode errorCode) {
		return docsRepository
			.findById(id)
			.orElseThrow(() -> new BumawikiException(errorCode));
	}

	public void checkTitleAlreadyExist(String title) {
		Optional<Docs> byTitle = docsRepository.findByTitle(title);
		if (byTitle.isPresent()) {
			throw DocsTitleAlreadyExistException.EXCEPTION;
		}
	}

	@Transactional
	public Docs createDocs(final DocsCreateRequestDto docsCreateRequestDto) {
		return docsRepository.save(
			Docs.builder()
				.title(docsCreateRequestDto.getTitle())
				.enroll(docsCreateRequestDto.getEnroll())
				.docsType(docsCreateRequestDto.getDocsType())
				.lastModifiedAt(LocalDateTime.now())
				.build()
		);
	}
}
