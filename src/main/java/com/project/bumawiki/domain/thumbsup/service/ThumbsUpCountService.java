package com.project.bumawiki.domain.thumbsup.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.bumawiki.domain.thumbsup.domain.repository.CustomThumbsUpRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ThumbsUpCountService {

	private final CustomThumbsUpRepository customThumbsUpRepository;

	public Long countThumbsUpByDocsTitle(String title) {
		return customThumbsUpRepository.countThumbsUpByTitle(title);
	}
}
