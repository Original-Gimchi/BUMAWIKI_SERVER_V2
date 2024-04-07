package com.project.bumawiki.domain.docs.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.bumawiki.domain.contribute.domain.Contribute;
import com.project.bumawiki.domain.contribute.service.ContributeService;
import com.project.bumawiki.domain.docs.domain.Docs;
import com.project.bumawiki.domain.docs.domain.VersionDocs;
import com.project.bumawiki.domain.docs.domain.repository.DocsRepositoryMapper;
import com.project.bumawiki.domain.docs.domain.repository.VersionDocsRepository;
import com.project.bumawiki.domain.docs.presentation.dto.request.DocsCreateRequestDto;
import com.project.bumawiki.domain.image.service.ImageService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class DocsCreateService {
	private final VersionDocsRepository versionDocsRepository;
	private final ImageService imageService;
	private final ContributeService contributeService;
	private final DocsRepositoryMapper docsRepositoryMapper;

	private static void setVersionDocs(final Docs docs, final VersionDocs savedVersionDocs,
		final Contribute contribute) {
		List<VersionDocs> versionDocs = new ArrayList<>();

		versionDocs.add(savedVersionDocs);

		savedVersionDocs.updateContributor(contribute);

		docs.setVersionDocs(versionDocs);
	}

	@Transactional
	public Long execute(final DocsCreateRequestDto docsCreateRequestDto) throws IOException {

		docsRepositoryMapper.checkTitleAlreadyExist(docsCreateRequestDto.getTitle());

		Docs docs = docsRepositoryMapper.createDocs(docsCreateRequestDto);

		VersionDocs savedVersionDocs = saveVersionDocs(docsCreateRequestDto, docs.getId());
		Contribute contribute = contributeService.setContribute(savedVersionDocs);

		setVersionDocs(docs, savedVersionDocs, contribute);

		return docs.getId();
	}

	/**
	 * 프론트가 [사진1]이라고 보낸거 우리가 저장한 이미지 주소로 바꾸는 로직
	 */
	public void setImageUrlInContents(final DocsCreateRequestDto docsCreateRequestDto, final ArrayList<String> urls) {
		String content = docsCreateRequestDto.getContents();
		for (String url : urls) {
			content = content.replaceFirst("<<사진>>", url);
		}
		docsCreateRequestDto.updateContent(content);
	}

	private VersionDocs saveVersionDocs(final DocsCreateRequestDto docsCreateRequestDto, final Long id) {
		return versionDocsRepository.save(
			VersionDocs.builder()
				.docsId(id)
				.contents(docsCreateRequestDto.getContents())
				.thisVersionCreatedAt(LocalDateTime.now())
				.version(1)
				.build()
		);
	}
}


