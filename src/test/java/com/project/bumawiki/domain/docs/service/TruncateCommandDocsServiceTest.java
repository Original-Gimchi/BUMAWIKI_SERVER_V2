package com.project.bumawiki.domain.docs.service;

import com.project.bumawiki.domain.docs.domain.Docs;
import com.project.bumawiki.domain.docs.domain.type.DocsType;
import com.project.bumawiki.domain.docs.implementation.DocsReader;

import com.project.bumawiki.domain.user.domain.User;

import com.project.bumawiki.domain.user.service.UserInfoService;

import com.project.bumawiki.global.test.SetUp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.*;

@SpringBootTest
class TruncateCommandDocsServiceTest {
	@Autowired
	private SetUp setUp;
	@Autowired
	private CommandDocsService commandDocsService;
	@Autowired
	private DocsReader docsReader;
	@Autowired
	private UserInfoService userInfoService;

	@BeforeEach
	public void setUp() {
		setUp.cleanUp();
	}

	@Test
	void 문서_생성_테스트() {
		// given
		Docs docs = new Docs("testTitle", 2024, DocsType.STUDENT);
		Docs duplicateDocs = new Docs("testTitle", 2023, DocsType.TEACHER);
		User user = userInfoService.findAnotherInfo(1L);

		//when
		commandDocsService.create(docs, user, "이건 테스트용 content 입니다");

		//then
		Docs checkDocs = docsReader.findByTitle(docs.getTitle());

		assertThat(checkDocs).isNotNull();

		commandDocsService.create(duplicateDocs, user, "이건 테스트용 content 입니다"); // exception
	}
}
