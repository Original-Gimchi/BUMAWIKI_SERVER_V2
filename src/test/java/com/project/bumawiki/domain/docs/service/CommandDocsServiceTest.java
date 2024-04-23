package com.project.bumawiki.domain.docs.service;

import static com.navercorp.fixturemonkey.api.experimental.JavaGetterMethodPropertySelector.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.RepeatedTest;
import org.springframework.beans.factory.annotation.Autowired;

import com.project.bumawiki.domain.docs.domain.Docs;
import com.project.bumawiki.domain.docs.domain.VersionDocs;
import com.project.bumawiki.domain.docs.domain.repository.DocsRepository;
import com.project.bumawiki.domain.docs.domain.repository.VersionDocsRepository;
import com.project.bumawiki.domain.docs.domain.type.DocsType;
import com.project.bumawiki.domain.thumbsup.domain.ThumbsUp;
import com.project.bumawiki.domain.thumbsup.domain.repository.ThumbsUpRepository;
import com.project.bumawiki.domain.user.domain.User;
import com.project.bumawiki.domain.user.domain.repository.UserRepository;
import com.project.bumawiki.global.error.exception.BumawikiException;
import com.project.bumawiki.global.error.exception.ErrorCode;
import com.project.bumawiki.global.service.FixtureGenerator;
import com.project.bumawiki.global.service.ServiceTest;

import com.navercorp.fixturemonkey.ArbitraryBuilder;

class CommandDocsServiceTest extends ServiceTest {
	@Autowired
	private CommandDocsService commandDocsService;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private DocsRepository docsRepository;
	@Autowired
	private VersionDocsRepository versionDocsRepository;
	@Autowired
	private ThumbsUpRepository thumbsUpRepository;
	@Autowired
	private QueryDocsService queryDocsService;

	@RepeatedTest(REPEAT_COUNT)
	void 문서_생성() {
		//given
		Docs docs = FixtureGenerator.getDefaultDocsBuilder().sample();
		User user = FixtureGenerator.getDefaultUserBuilder().sample();
		userRepository.save(user);
		String contents = fixtureGenerator.giveMeOne(String.class);

		//when
		commandDocsService.create(docs, user, contents);

		//then
		assertAll(() -> assertThat(docsRepository.findById(docs.getId()).get()).isEqualTo(docs),
			() -> assertThat(versionDocsRepository.findByDocs(docs).get(0).getContents()).isEqualTo(contents));
	}

	@RepeatedTest(REPEAT_COUNT)
	void 같은_이름_문서_생성_실패() {
		// given
		String title = FixtureGenerator.getDefaultStringBuilder().ofMaxLength(32).sample();
		String contents = FixtureGenerator.getDefaultStringBuilder().sample();

		Docs docs = FixtureGenerator.getDefaultDocsBuilder().set(javaGetter(Docs::getTitle), title).sample();

		docsRepository.save(docs);

		Docs savingDocs = FixtureGenerator.getDefaultDocsBuilder().set(javaGetter(Docs::getTitle), title).sample();

		User user = FixtureGenerator.getDefaultUserBuilder().sample();
		userRepository.save(user);

		// when
		BumawikiException exception = assertThrows(BumawikiException.class,
			() -> commandDocsService.create(savingDocs, user, contents));

		// then
		assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.DOCS_TITLE_ALREADY_EXIST);
	}

	@RepeatedTest(REPEAT_COUNT)
	void 문서_업데이트() {
		// given
		User user = FixtureGenerator.getDefaultUserBuilder().sample();
		userRepository.save(user);
		Docs docs = getDocsNotDocsType(List.of(DocsType.READONLY)).sample();
		docsRepository.save(docs);

		String contents = FixtureGenerator.getDefaultStringBuilder().sample();
		VersionDocs versionDocs = getFisrtVersionDocs(docs, user);
		versionDocsRepository.save(versionDocs);

		// when
		commandDocsService.update(user, docs.getTitle(), contents, versionDocs.getVersion());

		// then
		assertAll(
			() -> assertThat(versionDocsRepository.findFirstByDocsOrderByVersionDesc(docs).getContents()).isEqualTo(
				contents), () -> assertThat(versionDocsRepository.count()).isEqualTo(2));
	}

	@RepeatedTest(REPEAT_COUNT)
	void 문서_삭제() {
		// given
		Docs docs = FixtureGenerator.getDefaultDocsBuilder().sample();
		docsRepository.save(docs);

		User user = FixtureGenerator.getDefaultUserBuilder().sample();
		userRepository.save(user);

		List<ThumbsUp> thumbsUpList = FixtureGenerator.getDefaultThumbsUpBuilder(docs, user).sampleList(5);

		thumbsUpRepository.saveAll(thumbsUpList);

		// when
		commandDocsService.delete(docs.getId());

		// then
		assertAll(() -> assertThat(docsRepository.findById(docs.getId())).isEmpty(),
			() -> assertThat(thumbsUpRepository.count()).isZero());
	}

	@RepeatedTest(REPEAT_COUNT)
	void 문서_타입_업데이트() {
		// given
		Docs docs = FixtureGenerator.getDefaultDocsBuilder().sample();
		docsRepository.save(docs);
		DocsType docsType = fixtureGenerator.giveMeBuilder(DocsType.class)
			.setPostCondition("$", DocsType.class, it -> !it.equals(docs.getDocsType()))
			.sample();

		// when
		commandDocsService.docsTypeUpdate(docs.getId(), docsType);

		// then
		assertThat(docsRepository.findById(docs.getId()).get().getDocsType()).isEqualTo(docsType);
	}

	@RepeatedTest(REPEAT_COUNT)
	void 일반문서_중_본인이름_포함_시_문서_업데이트_실패() {
		// given
		String name = FixtureGenerator.getDefaultStringBuilder().ofLength(3).sample();
		User user = FixtureGenerator.getDefaultUserBuilder().set(javaGetter(User::getName), name).sample();
		userRepository.save(user);
		Docs docs = getDocsNotDocsType(List.of(DocsType.READONLY, DocsType.STUDENT)).set(javaGetter(Docs::getTitle),
			name).sample();
		docsRepository.save(docs);
		String contents = FixtureGenerator.getDefaultStringBuilder().sample();
		VersionDocs versionDocs = getFisrtVersionDocs(docs, user);
		versionDocsRepository.save(versionDocs);

		// when, then
		BumawikiException exception = assertThrows(BumawikiException.class,
			() -> commandDocsService.update(user, docs.getTitle(), contents, versionDocs.getVersion()));

		assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.CANNOT_CHANGE_YOUR_DOCS);
	}

	@RepeatedTest(REPEAT_COUNT)
	void 학생문서_중_본인문서_업데이트_실패() {
		// given
		String name = FixtureGenerator.getDefaultStringBuilder().ofLength(3).sample();
		User user = FixtureGenerator.getDefaultUserBuilder().set(javaGetter(User::getName), name)
			.setNotNull(javaGetter(User::getEnroll)).sample();
		userRepository.save(user);
		Docs docs = FixtureGenerator.getDefaultDocsBuilder()
			.set(javaGetter(Docs::getDocsType), DocsType.STUDENT)
			.set(javaGetter(Docs::getTitle), name)
			.set(javaGetter(Docs::getEnroll), user.getEnroll()).sample();
		docsRepository.save(docs);
		String contents = FixtureGenerator.getDefaultStringBuilder().sample();
		VersionDocs versionDocs = getFisrtVersionDocs(docs, user);
		versionDocsRepository.save(versionDocs);

		// when, then
		BumawikiException exception = assertThrows(BumawikiException.class,
			() -> commandDocsService.update(user, docs.getTitle(), contents, versionDocs.getVersion()));

		assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.CANNOT_CHANGE_YOUR_DOCS);
	}

	@RepeatedTest(REPEAT_COUNT)
	void 읽기전용_문서_업데이트_실패() {
		// given
		User user = FixtureGenerator.getDefaultUserBuilder().sample();
		userRepository.save(user);

		Docs docs = FixtureGenerator.getDefaultDocsBuilder()
			.set(javaGetter(Docs::getDocsType), DocsType.READONLY)
			.sample();

		docsRepository.save(docs);

		String contents = FixtureGenerator.getDefaultStringBuilder().sample();
		VersionDocs versionDocs = getFisrtVersionDocs(docs, user);
		versionDocsRepository.save(versionDocs);

		// when
		BumawikiException exception = assertThrows(BumawikiException.class,
			() -> commandDocsService.update(user, docs.getTitle(), contents, versionDocs.getVersion()));

		assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.NO_UPDATABLE_DOCS);
	}

	@RepeatedTest(REPEAT_COUNT)
	void 버전_불일치_문서_업데이트_실패() {
		// given
		User user = FixtureGenerator.getDefaultUserBuilder().sample();
		userRepository.save(user);

		Docs docs = getDocsNotDocsType(List.of(DocsType.READONLY)).sample();

		docsRepository.save(docs);

		String contents = FixtureGenerator.getDefaultStringBuilder().sample();
		VersionDocs versionDocs = getFisrtVersionDocs(docs, user);
		versionDocsRepository.save(versionDocs);

		// when
		BumawikiException exception = assertThrows(BumawikiException.class,
			() -> commandDocsService.update(user, docs.getTitle(), contents, versionDocs.getVersion() + 1));

		assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.DOCS_CONFLICTED);
	}

	@RepeatedTest(REPEAT_COUNT)
	void 문서_이름_수정() {
		//given
		Docs savedDocs = FixtureGenerator.getDefaultDocsBuilder().sample();
		docsRepository.save(savedDocs);

		String newTitle = FixtureGenerator.getDefaultStringBuilder()
			.excludeChars(savedDocs.getTitle().toCharArray())
			.sample();

		//when
		commandDocsService.titleUpdate(savedDocs.getTitle(), newTitle);

		//then
		assertThat(savedDocs.getTitle()).isEqualTo(newTitle);
	}

	@RepeatedTest(REPEAT_COUNT)
	void 있는_문서_이름_수정_실패() {
		//given
		String title = FixtureGenerator.getDefaultStringBuilder().ofMaxLength(32).sample();
		;

		Docs docs = FixtureGenerator.getDefaultDocsBuilder().set(javaGetter(Docs::getTitle), title).sample();

		docsRepository.save(docs);
		//when, then
		BumawikiException exception = assertThrows(BumawikiException.class,
			() -> commandDocsService.titleUpdate(docs.getTitle(), title));

		assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.DOCS_TITLE_ALREADY_EXIST);
	}

	@RepeatedTest(REPEAT_COUNT)
	void 문서_충돌_해결() {
		// given
		Docs docs = FixtureGenerator.getDefaultDocsBuilder().sample();
		docsRepository.save(docs);

		User user = FixtureGenerator.getDefaultUserBuilder().sample();
		userRepository.save(user);

		VersionDocs versionDocs = getFisrtVersionDocs(docs, user);
		versionDocsRepository.save(versionDocs);
		String contents = FixtureGenerator.getDefaultStringBuilder().sample();

		// when
		commandDocsService.solveConflict(docs.getTitle(), contents, versionDocs.getVersion(), user);

		// then
		assertThat(versionDocsRepository.findFirstByDocsOrderByVersionDesc(docs).getVersion()).isEqualTo(
			versionDocs.getVersion() + 1);
	}

	private ArbitraryBuilder<Docs> getDocsNotDocsType(List<DocsType> docsTypes) {
		return FixtureGenerator.getDefaultDocsBuilder()
			.setPostCondition(javaGetter(Docs::getDocsType), DocsType.class,
				it -> docsTypes.stream().noneMatch(it::equals));
	}

	private VersionDocs getFisrtVersionDocs(Docs docs, User user) {
		return FixtureGenerator.getDefaultVersionDocsBuilder(docs, user)
			.set(javaGetter(VersionDocs::getVersion), 0)
			.sample();
	}

}
