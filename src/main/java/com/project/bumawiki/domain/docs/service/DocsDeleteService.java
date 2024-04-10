package com.project.bumawiki.domain.docs.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.bumawiki.domain.docs.implementation.DocsDeleter;
import com.project.bumawiki.domain.thumbsup.implementation.ThumbsUpDeleter;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class DocsDeleteService {
	private final DocsDeleter docsDeleter;
	private final ThumbsUpDeleter thumbsUpDeleter;

	public void execute(Long id) {
		docsDeleter.delete(id);
		thumbsUpDeleter.deleteAllByDocsId(id);
	}
}
