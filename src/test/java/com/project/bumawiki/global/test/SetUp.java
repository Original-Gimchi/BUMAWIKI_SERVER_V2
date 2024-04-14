package com.project.bumawiki.global.test;

import com.project.bumawiki.domain.user.domain.User;

import com.project.bumawiki.domain.user.domain.authority.Authority;

import jakarta.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class SetUp {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private EntityManager entityManager;

	@Transactional
	public void cleanUp() {
		jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY FALSE");

		entityManager.getMetamodel().getEntities().stream().map(entity ->{
			jdbcTemplate.execute("TRUNCATE TABLE "+ entity.getName());
			return null;
		});

		jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY TRUE");

		setUpTestData();
	}

	private void setUpTestData() {
		// Docs docs = new Docs("testTitle", 2024, DocsType.STUDENT);
		// entityManager.persist(docs);

		User user = User.builder()
			.name("test")
			.email("test@bssm.hs.kr")
			.enroll(2024)
			.authority(Authority.USER)
			.nickName("test nickname")
			.build();
		entityManager.persist(user);
	}
}
