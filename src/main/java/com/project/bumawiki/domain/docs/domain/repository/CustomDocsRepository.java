package com.project.bumawiki.domain.docs.domain.repository;

import java.util.List;

import com.project.bumawiki.domain.docs.domain.Docs;
import com.project.bumawiki.domain.docs.presentation.dto.response.DocsPopularResponseDto;
import com.project.bumawiki.domain.docs.presentation.dto.response.VersionResponseDto;

public interface CustomDocsRepository {

	VersionResponseDto getDocsVersion(Docs docs);

	List<DocsPopularResponseDto> findByThumbsUpsDesc();
}
