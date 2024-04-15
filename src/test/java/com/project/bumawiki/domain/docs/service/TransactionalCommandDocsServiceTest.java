package com.project.bumawiki.domain.docs.service;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import com.project.bumawiki.domain.user.service.UserInfoService;
import com.project.bumawiki.global.test.SetUpTestData;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.project.bumawiki.domain.docs.domain.Docs;
import com.project.bumawiki.domain.docs.domain.type.DocsType;
import com.project.bumawiki.domain.docs.implementation.DocsReader;
import com.project.bumawiki.domain.user.domain.User;

@SpringBootTest
@Transactional
class TransactionalCommandDocsServiceTest {

	@Autowired
	private CommandDocsService commandDocsService;

	@Autowired
	private DocsReader docsReader;

	@Autowired
	private SetUpTestData setUpTestData;

	@Autowired
	private UserInfoService userInfoService;

	@BeforeEach
	public void setUp() {
		setUpTestData.setUpTestData();
	}

	@Test
	void create() {
		// given
		Docs docs = new Docs("부마코인 1000만원 달성", 2024, DocsType.ACCIDENT);
		String contents = "ㄷㄷㄷㄷ";
		User user = userInfoService.findAnotherInfo(1L);

		// when
		commandDocsService.create(docs, user, contents);

		// then
		Docs findDocs = docsReader.findByTitle(docs.getTitle());
		assertThat(findDocs).isNotNull();
	}

	@Test
	void title_update() {
		User user = userInfoService.findAnotherInfo(2L);
		Docs docs = docsReader.findById(1L);
		// commandDocsService.titleUpdate(docs.getTitle(), "update title");
		// assertThat(docs.getTitle()).isEqualTo("update title");
	}
}
