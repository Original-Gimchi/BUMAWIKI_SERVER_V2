package com.project.bumawiki.domain.thumbsup.implementation;

import java.util.List;

import com.project.bumawiki.domain.thumbsup.domain.ThumbsUp;
import com.project.bumawiki.domain.thumbsup.domain.repository.ThumbsUpRepository;
import com.project.bumawiki.global.annotation.Implementation;

import lombok.RequiredArgsConstructor;

@Implementation
@RequiredArgsConstructor
public class ThumbsUpDeleter {
	private final ThumbsUpRepository thumbsUpRepository;

	public void deleteAllByDocsId(Long docsId) {
		List<ThumbsUp> thumbsUps = thumbsUpRepository.findByDocs_Id(docsId);
		thumbsUpRepository.deleteAllInBatch(thumbsUps);
	}
}
