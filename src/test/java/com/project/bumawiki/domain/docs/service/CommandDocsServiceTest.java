package com.project.bumawiki.domain.docs.service;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.project.bumawiki.domain.docs.domain.Docs;
import com.project.bumawiki.domain.docs.domain.VersionDocs;
import com.project.bumawiki.domain.docs.domain.repository.DocsRepository;
import com.project.bumawiki.domain.docs.domain.repository.VersionDocsRepository;
import com.project.bumawiki.domain.docs.domain.type.DocsType;
import com.project.bumawiki.domain.user.domain.User;
import com.project.bumawiki.domain.user.domain.authority.Authority;
import com.project.bumawiki.domain.user.domain.repository.UserRepository;
import com.project.bumawiki.global.annotation.ServiceTest;

@ServiceTest
class CommandDocsServiceTest {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private CommandDocsService commandDocsService;
	@Autowired
	private DocsRepository docsRepository;
	@Autowired
	private VersionDocsRepository versionDocsRepository;

	@Test
	void 문서_생성하기() {
		// given
		User user = User.builder()
			.email("2022038@bssm.hs.kr")
			.name("마현우")
			.enroll(2022)
			.nickName("마현우")
			.authority(Authority.USER)
			.build();
		userRepository.save(user);

		String title = "제목";
		String contents = "본문";
		int enroll = 2024;
		Docs docs = new Docs(title, enroll, DocsType.ACCIDENT);

		// when
		commandDocsService.create(docs, user, contents);

		// then
		assertThat(docsRepository.findByTitle(title).get().getTitle()).isEqualTo(title);
		assertThat(versionDocsRepository.findFirstByDocsOrderByVersionDesc(docs).getContents()).isEqualTo(contents);
	}

	@Test
	void 문서_업데이트하기() {
		// given
		User user = User.builder()
			.email("2022038@bssm.hs.kr")
			.name("마현우")
			.enroll(2022)
			.nickName("마현우")
			.authority(Authority.USER)
			.build();
		userRepository.save(user);

		String title = "제목";
		String contents = "본문";
		String updatedContents = "본문본문";
		int enroll = 2024;
		Docs docs = new Docs(title, enroll, DocsType.ACCIDENT);
		commandDocsService.create(docs, user, contents);

		// when
		commandDocsService.update(user, title, updatedContents, 0);

		// then
		VersionDocs versionDocs = versionDocsRepository.findFirstByDocsOrderByVersionDesc(docs);
		assertThat(versionDocs.getContents()).isEqualTo(updatedContents);
		assertThat(versionDocs.getVersion()).isEqualTo(1);
	}
}