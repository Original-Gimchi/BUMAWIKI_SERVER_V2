package com.project.bumawiki.domain.docs.domain.repository;

import static com.project.bumawiki.domain.contribute.domain.QContribute.*;
import static com.project.bumawiki.domain.docs.domain.QDocs.*;
import static com.project.bumawiki.domain.docs.domain.QVersionDocs.*;
import static com.project.bumawiki.domain.thumbsup.domain.QThumbsUp.*;
import static com.project.bumawiki.domain.user.domain.QUser.*;
import static com.querydsl.core.types.Projections.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.project.bumawiki.domain.docs.domain.Docs;
import com.project.bumawiki.domain.docs.presentation.dto.response.DocsPopularResponseDto;
import com.project.bumawiki.domain.docs.presentation.dto.response.VersionDocsResponseDto;
import com.project.bumawiki.domain.docs.presentation.dto.response.VersionResponseDto;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class CustomDocsRepositoryImpl implements CustomDocsRepository {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public VersionResponseDto getDocsVersion(Docs findDocs) {
		List<VersionDocsResponseDto> versionDocsResponseDto = jpaQueryFactory
			.select(constructor(VersionDocsResponseDto.class, versionDocs.thisVersionCreatedAt, user.id,
				user.nickName))
			.from(docs)
			.join(docs.docsVersion, versionDocs)
			.join(versionDocs.contributor, contribute)
			.join(contribute.contributor, user)
			.where(docs.id.eq(findDocs.getId()))
			.distinct()
			.orderBy(versionDocs.thisVersionCreatedAt.desc())
			.fetch();

		return new VersionResponseDto(versionDocsResponseDto, findDocs);
	}

	@Override
	public List<DocsPopularResponseDto> findByThumbsUpsDesc() {
		return jpaQueryFactory
			.select(
				constructor(DocsPopularResponseDto.class, docs.title, docs.enroll, docs.docsType, thumbsUp.id.count()))
			.from(docs)
			.innerJoin(docs.thumbsUps, thumbsUp)
			.groupBy(docs.title, docs.enroll, docs.docsType)
			.orderBy(thumbsUp.id.count().desc())
			.limit(25)
			.fetch();

	}
}
