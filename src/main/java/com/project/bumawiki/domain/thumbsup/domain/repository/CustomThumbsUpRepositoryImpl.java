package com.project.bumawiki.domain.thumbsup.domain.repository;

import static com.project.bumawiki.domain.docs.domain.QDocs.*;
import static com.project.bumawiki.domain.thumbsup.domain.QThumbsUp.*;
import static com.querydsl.core.types.Projections.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.project.bumawiki.domain.thumbsup.presentation.dto.ThumbsUpResponseDto;
import com.project.bumawiki.domain.user.domain.User;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CustomThumbsUpRepositoryImpl implements CustomThumbsUpRepository {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public List<ThumbsUpResponseDto> getUserThumbsUp(User user) {
		return jpaQueryFactory
			.select(constructor(ThumbsUpResponseDto.class, docs))
			.from(thumbsUp)
			.leftJoin(thumbsUp.docs, docs)
			.where(thumbsUp.user.eq(user))
			.distinct()
			.fetch();
	}
}
