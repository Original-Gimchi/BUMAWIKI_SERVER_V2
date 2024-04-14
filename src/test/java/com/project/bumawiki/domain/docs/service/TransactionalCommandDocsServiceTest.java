package com.project.bumawiki.domain.docs.service;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.project.bumawiki.domain.docs.domain.Docs;
import com.project.bumawiki.domain.docs.domain.type.DocsType;
import com.project.bumawiki.domain.docs.implementation.DocsReader;
import com.project.bumawiki.domain.user.domain.User;
import com.project.bumawiki.domain.user.domain.authority.Authority;
import com.project.bumawiki.domain.user.domain.repository.UserRepository;

@Transactional
@SpringBootTest
class TransactionalCommandDocsServiceTest {

	@Autowired
	private CommandDocsService commandDocsService;

	@Autowired
	private DocsReader docsReader;

	@Autowired
	private UserRepository userRepository;

	@Test
	void create() {
		// given
		Docs docs = new Docs("부마코인 1000만원 달성", 2024, DocsType.ACCIDENT);
		User user = User.builder()
			.email("2022038@bssm.hs.kr")
			.name("마현우")
			.enroll(2022)
			.nickName("마현우")
			.authority(Authority.USER)
			.build();
		String contents = "실시간 부붕이 X됐다 지금 시세 뭐냐 이거";

		// when
		userRepository.save(user);
		commandDocsService.create(docs, user, contents);

		// then
		Docs findDocs = docsReader.findByTitle(docs.getTitle());
		assertThat(findDocs).isNotNull();
	}
}
