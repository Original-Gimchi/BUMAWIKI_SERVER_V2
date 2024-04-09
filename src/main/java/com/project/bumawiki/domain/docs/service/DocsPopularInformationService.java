package com.project.bumawiki.domain.docs.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.bumawiki.domain.docs.domain.repository.DocsRepository;
import com.project.bumawiki.domain.docs.presentation.dto.response.DocsPopularResponseDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DocsPopularInformationService {
	private final DocsRepository docsRepository;

	@Transactional(readOnly = true)
	public List<DocsPopularResponseDto> getDocsByPopular() {
		return docsRepository.findByThumbsUpsDesc();
	}
}
