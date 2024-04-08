package com.project.bumawiki.global.config.querydsl;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class QueryDslConfig {

	private final EntityManager em;

	@Bean
	public JPAQueryFactory queryFactory() {
		return new JPAQueryFactory(em);
	}
}
