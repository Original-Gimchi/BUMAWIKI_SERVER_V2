package com.project.bumawiki.domain.thumbsup.infra;

import static com.project.bumawiki.domain.docs.domain.QDocs.*;
import static com.project.bumawiki.domain.thumbsup.domain.QThumbsUp.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.project.bumawiki.domain.docs.domain.Docs;
import com.project.bumawiki.domain.thumbsup.domain.repository.CustomThumbsUpRepository;
import com.project.bumawiki.domain.user.domain.User;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CustomThumbsUpRepositoryImpl implements CustomThumbsUpRepository {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public List<Docs> getUserThumbsUp(User user) {
		return jpaQueryFactory
			.select(docs)
			.from(thumbsUp)
			.leftJoin(thumbsUp.docs, docs)
			.where(thumbsUp.user.eq(user))
			.distinct()
			.fetch();
	}
}
