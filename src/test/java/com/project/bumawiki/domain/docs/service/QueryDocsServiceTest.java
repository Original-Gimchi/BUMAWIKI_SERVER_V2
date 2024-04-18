package com.project.bumawiki.domain.docs.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.project.bumawiki.domain.docs.domain.Docs;
import com.project.bumawiki.domain.docs.domain.VersionDocs;
import com.project.bumawiki.domain.docs.domain.repository.DocsRepository;
import com.project.bumawiki.domain.docs.domain.repository.VersionDocsRepository;
import com.project.bumawiki.domain.docs.domain.type.DocsType;
import com.project.bumawiki.domain.docs.presentation.dto.response.DocsResponseDto;
import com.project.bumawiki.domain.docs.presentation.dto.response.VersionResponseDto;
import com.project.bumawiki.domain.user.domain.User;
import com.project.bumawiki.domain.user.domain.authority.Authority;
import com.project.bumawiki.domain.user.domain.repository.UserRepository;
import com.project.bumawiki.global.service.ServiceTest;

class QueryDocsServiceTest extends ServiceTest {

	@Autowired
	private DocsRepository docsRepository;

	@Autowired
	private QueryDocsService queryDocsService;

	@Autowired
	private VersionDocsRepository versionDocsRepository;

	@Autowired
	private UserRepository userRepository;

	@Test
	void 제목으로_문서_상세_조회하기() {
		// given
		User user = fixtureGenerator.giveMeBuilder(User.class)
			.setNull("id")
			.set("authority", Authority.USER)
			.setNull("thumbsUps")
			.sample();

		userRepository.save(user);

		Docs docs = fixtureGenerator.giveMeBuilder(Docs.class)
			.setNull("id")
			.setNull("versionDocs")
			.sample();
		String title = docs.getTitle();

		docsRepository.save(docs);

		VersionDocs versionDocs = fixtureGenerator.giveMeBuilder(VersionDocs.class)
			.set("version", 1)
			.set("docs", docs)
			.set("user", user)
			.sample();

		versionDocsRepository.save(versionDocs);

		// when
		DocsResponseDto dto = queryDocsService.findDocs(title);

		// then
		assertAll(
			() -> {
				assertThat(dto).isNotNull();
				assertThat(dto.title()).isEqualTo(title);
			}
		);
	}

	@Test
	void 제목으로_문서_역사_조회하기() {
		// given
		User user = fixtureGenerator.giveMeBuilder(User.class)
			.setNull("id")
			.set("authority", Authority.USER)
			.setNull("thumbsUps")
			.sample();

		userRepository.save(user);

		Docs docs = fixtureGenerator.giveMeBuilder(Docs.class)
			.setNull("id")
			.setNull("versionDocs")
			.sample();
		String title = docs.getTitle();

		docsRepository.save(docs);

		List<VersionDocs> versionDocsList = fixtureGenerator.giveMeBuilder(VersionDocs.class)
			.set("docs", docs)
			.set("user", user)
			.sampleList(10);

		versionDocsRepository.saveAll(versionDocsList);

		// when
		VersionResponseDto dto = queryDocsService.findDocsVersion(title);

		// then
		assertAll(
			() -> {
				assertThat(dto.versionDocsResponseDto()).isNotNull();
				assertThat(dto.length()).isEqualTo(dto.versionDocsResponseDto().size());
			},
			() -> assertThat(dto.docsType()).isEqualTo(docs.getDocsType()),
			() -> assertThat(dto.title()).isEqualTo(title)
		);
	}

	@Test
	void 문서_타입으로_조회하기() {
		// given
		User user = fixtureGenerator.giveMeBuilder(User.class)
			.setNull("id")
			.set("authority", Authority.USER)
			.setNull("thumbsUps")
			.sample();

		userRepository.save(user);

		List<Docs> docsList = fixtureGenerator.giveMeBuilder(Docs.class)
			.setNull("id")
			.setNull("versionDocs")
			.sampleList(30);

		docsRepository.saveAll(docsList);

		Random random = new Random();

		// when
		DocsType randomDocsType = DocsType.values()[random.nextInt(DocsType.values().length)];
		List<Docs> findDocsList = queryDocsService.findByDocsTypeOrderByEnroll(randomDocsType);

		// then
		assertAll(
			() -> assertThat(docsList.containsAll(findDocsList)).isTrue(),
			() -> assertThat(findDocsList.get(random.nextInt(findDocsList.size())).getDocsType()).isEqualTo(randomDocsType)
		);
	}
}
