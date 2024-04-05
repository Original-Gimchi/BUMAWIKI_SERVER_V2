package com.project.bumawiki.domain.docs.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.bumawiki.domain.docs.domain.repository.DocsRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class DocsDeleteService {
	private final DocsRepository docsRepository;

	public Long execute(Long id) {
		docsRepository.deleteById(id);

		return id;
	}
}
