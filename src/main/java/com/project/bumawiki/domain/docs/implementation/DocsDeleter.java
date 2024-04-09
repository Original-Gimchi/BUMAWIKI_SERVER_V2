package com.project.bumawiki.domain.docs.implementation;

import com.project.bumawiki.domain.docs.domain.repository.DocsRepository;
import com.project.bumawiki.global.annotation.Implementation;

import lombok.RequiredArgsConstructor;

@Implementation
@RequiredArgsConstructor
public class DocsDeleter {
	private final DocsRepository docsRepository;

	public void delete(Long id) {
		docsRepository.deleteById(id);
	}
}
