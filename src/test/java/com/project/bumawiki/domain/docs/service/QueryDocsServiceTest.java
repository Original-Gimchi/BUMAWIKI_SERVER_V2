package com.project.bumawiki.domain.docs.service;

import static com.navercorp.fixturemonkey.api.experimental.JavaGetterMethodPropertySelector.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Comparator;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;

import net.jqwik.api.Arbitraries;

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

import com.navercorp.fixturemonkey.ArbitraryBuilder;

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
			() -> assertThat(findDocsList.get(random.nextInt(findDocsList.size())).getDocsType()).isEqualTo(
				randomDocsType)
		);
	}

	@Test
	void 유저가_수정한_버전문서_조회() {
		// given
		User user = fixtureGenerator.giveMeBuilder(User.class)
			.setNull("id")
			.set("authority", Authority.USER)
			.setNull("thumbsUps")
			.sample();

		User otherUser = fixtureGenerator.giveMeBuilder(User.class)
			.setNull("id")
			.set("authority", Authority.USER)
			.setNull("thumbsUps")
			.sample();

		userRepository.save(user);
		userRepository.save(otherUser);

		Docs docs = getDefaultDocsBuilder().sample();
		docsRepository.save(docs);

		List<VersionDocs> versionDocsList = fixtureGenerator.giveMeBuilder(VersionDocs.class)
			.set("user", user)
			.set(javaGetter(VersionDocs::getDocs), docs)
			.sampleList(10);

		List<VersionDocs> versionDocsList2 = fixtureGenerator.giveMeBuilder(VersionDocs.class)
			.set("user", otherUser)
			.set(javaGetter(VersionDocs::getDocs), docs)
			.sampleList(10);

		versionDocsRepository.saveAll(versionDocsList);
		versionDocsRepository.saveAll(versionDocsList2);

		// when
		List<VersionDocs> findVersionDocsList = queryDocsService.findAllVersionDocsByUser(user);

		// then
		assertAll(
			() -> assertThat(findVersionDocsList.stream()
				.allMatch(versionDocs -> versionDocs.getUser().getId().equals(user.getId()))).isTrue()
		);

	}

	@Test
	public void 최근_수정한_문서_10개_조회() {
		//given
		User user = getDefaultUserBuilder().sample();
		userRepository.save(user);

		List<Docs> docsList = fixtureGenerator.giveMeBuilder(Docs.class)
			.setNull("id")
			.setNull("versionDocs")
			.setNotNull("lastModifiedAt")
			.sampleList(20);

		docsRepository.saveAll(docsList);

		List<VersionDocs> versionDocsList = fixtureGenerator.giveMeBuilder(VersionDocs.class)
			.set("user", user)
			.set("docs", Arbitraries.of(docsList))
			.setNotNull("createdAt")
			.sampleList(40);

		versionDocsRepository.saveAll(versionDocsList);

		versionDocsList.forEach(versionDocs -> versionDocs.getDocs().updateModifiedAt(versionDocs.getCreatedAt()));

		List<Long> sortedDocsList = docsList.stream()
			.sorted(Comparator.comparing(Docs::getLastModifiedAt).reversed())
			.map(Docs::getId)
			.distinct()
			.toList();

		sortedDocsList = sortedDocsList.subList(0, Math.min(10, sortedDocsList.size()));
		List<Long> finalSortedDocsList = sortedDocsList;
		//when
		List<Docs> findDocsList = queryDocsService.showDocsModifiedAtDesc(Pageable.ofSize(10));

		//then
		assertAll(
			() -> assertThat(findDocsList.stream().map(Docs::getId).toList())
				.containsAll(finalSortedDocsList),
			() -> assertThat(findDocsList.size()).isLessThanOrEqualTo(10)
		);

	}

	private ArbitraryBuilder<Docs> getDefaultDocsBuilder() {
		return fixtureGenerator.giveMeBuilder(Docs.class)
			.setNull("id")
			.setNull("versionDocs")
			.setPostCondition("docsType", DocsType.class, (it) -> it != DocsType.READONLY);
	}

	private Docs getTitleAndEnrollDocs(String Title, int enroll) {
		return getDefaultDocsBuilder()
			.set("title", Title)
			.set("enroll", enroll)
			.sample();
	}

	private ArbitraryBuilder<User> getDefaultUserBuilder() {
		return fixtureGenerator.giveMeBuilder(User.class)
			.setNull("id")
			.set("authority", Authority.USER)
			.setNull("thumbsUps");
	}

}
