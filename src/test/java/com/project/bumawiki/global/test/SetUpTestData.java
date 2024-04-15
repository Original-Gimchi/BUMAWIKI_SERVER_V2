package com.project.bumawiki.global.test;

import com.project.bumawiki.domain.user.domain.User;
import com.project.bumawiki.domain.user.domain.authority.Authority;

import jakarta.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class SetUpTestData {

	@Autowired
	private EntityManager entityManager;

	@Transactional
	public void setUpTestData() {
		// Docs docs = new Docs("testTitle", 2024, DocsType.STUDENT);
		// entityManager.persist(docs);

		User user = User.builder()
			.name("test")
			.email("test1@bssm.hs.kr")
			.enroll(2024)
			.authority(Authority.USER)
			.nickName("test nickname")
			.build();
		entityManager.persist(user);
	}
}
