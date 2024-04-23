package com.project.bumawiki.domain.docs.service;

import static com.navercorp.fixturemonkey.api.experimental.JavaGetterMethodPropertySelector.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.RepeatedTest;
import org.springframework.beans.factory.annotation.Autowired;

import net.jqwik.api.Arbitraries;

import com.project.bumawiki.domain.docs.domain.Docs;
import com.project.bumawiki.domain.docs.domain.VersionDocs;
import com.project.bumawiki.domain.docs.domain.repository.DocsRepository;
import com.project.bumawiki.domain.docs.domain.repository.VersionDocsRepository;
import com.project.bumawiki.domain.docs.domain.type.DocsType;
import com.project.bumawiki.domain.docs.presentation.dto.response.DocsPopularResponseDto;
import com.project.bumawiki.domain.thumbsup.domain.repository.ThumbsUpRepository;
import com.project.bumawiki.domain.user.domain.User;
import com.project.bumawiki.domain.user.domain.repository.UserRepository;
import com.project.bumawiki.global.error.exception.BumawikiException;
import com.project.bumawiki.global.error.exception.ErrorCode;
import com.project.bumawiki.global.service.FixtureGenerator;
import com.project.bumawiki.global.service.ServiceTest;

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
		Docs docs = getDocs();
		User user = getSavedUserWithOutThumbsUp();
		String contents = fixtureGenerator.giveMeOne(String.class);

		//when
		commandDocsService.create(docs, user, contents);

		//then
		assertAll(
			() -> assertThat(docsRepository.findById(docs.getId()).get()).isEqualTo(docs),
			() -> assertThat(versionDocsRepository.findByDocs(docs).get(0).getContents()).isEqualTo(contents)
		);
	}

	@RepeatedTest(REPEAT_COUNT)
	void 같은_이름_문서_생성_실패() {
		// given
		String title = getTitle();
		String contents = fixtureGenerator.giveMeOne(String.class);
		getSavedDocs(title);
		Docs savingDocs = getDocs(title);
		User user = getSavedUserWithOutThumbsUp();

		// when
		BumawikiException exception = assertThrows(BumawikiException.class,
			() -> commandDocsService.create(savingDocs, user, contents)
		);

		// then
		assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.DOCS_TITLE_ALREADY_EXIST);
	}



	@RepeatedTest(REPEAT_COUNT)
	void 문서_업데이트() {
		// given
		User user = getSavedUserWithOutThumbsUp();
		Docs docs = getSavedDocsNotReadonly();
		String contents = FixtureGenerator.getDefaultStringBuilder().sample();
		VersionDocs versionDocs = getSavedVersionDocs(docs, user);

		// when
		commandDocsService.update(user, docs.getTitle(), contents, versionDocs.getVersion());

		// then
		assertAll(
			() -> assertThat(
				versionDocsRepository.findFirstByDocsOrderByVersionDesc(docs).getContents()
			).isEqualTo(contents),
			() -> assertThat(versionDocsRepository.count()).isEqualTo(2)
		);
	}

	@RepeatedTest(REPEAT_COUNT)
	void 문서_삭제() {
		// given
		Docs docs = getSavedDocs();
		User user = getSavedUserWithOutThumbsUp();
		getSavedThumbsUpList(docs, user);

		// when
		commandDocsService.delete(docs.getId());

		// then
		assertAll(
			() -> assertThat(docsRepository.findById(docs.getId())).isEmpty(),
			() -> assertThat(thumbsUpRepository.count()).isZero()
		);
	}

	@RepeatedTest(REPEAT_COUNT)
	void 문서_타입_업데이트() {
		// given
		Docs docs = getSavedDocs();
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
		User user = getSavedUserWithOutThumbsUp(name);
		Docs docs = getSavedNotStudentDocs(name);
		String contents = FixtureGenerator.getDefaultStringBuilder().sample();
		VersionDocs versionDocs = getSavedVersionDocs(docs, user);

		// when, then
		BumawikiException exception = assertThrows(BumawikiException.class,
			() -> commandDocsService.update(user, docs.getTitle(), contents, versionDocs.getVersion())
		);

		assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.CANNOT_CHANGE_YOUR_DOCS);
	}

	@RepeatedTest(REPEAT_COUNT)
	void 학생문서_중_본인문서_업데이트_실패() {
		// given
		String name = FixtureGenerator.getDefaultStringBuilder().ofLength(3).sample();
		User user = getSavedUserWithOutThumbsUp(name);
		Docs docs = getSavedNotStudentDocs(name);
		String contents =FixtureGenerator.getDefaultStringBuilder().sample();
		VersionDocs versionDocs = getSavedVersionDocs(docs, user);

		// when, then
		BumawikiException exception = assertThrows(BumawikiException.class,
			() -> commandDocsService.update(user, docs.getTitle(), contents, versionDocs.getVersion())
		);

		assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.CANNOT_CHANGE_YOUR_DOCS);
	}

	@RepeatedTest(REPEAT_COUNT)
	void 읽기전용_문서_업데이트_실패() {
		// given
		User user = getSavedUserWithOutThumbsUp();
		Docs docs = getSavedDocsReadonly();
		String contents = FixtureGenerator.getDefaultStringBuilder().sample();
		VersionDocs versionDocs = getSavedVersionDocs(docs, user);

		// when
		BumawikiException exception = assertThrows(BumawikiException.class,
			() -> commandDocsService.update(user, docs.getTitle(), contents, versionDocs.getVersion())
		);

		assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.NO_UPDATABLE_DOCS);
	}

	@RepeatedTest(REPEAT_COUNT)
	void 버전_불일치_문서_업데이트_실패() {
		// given
		User user = getSavedUserWithOutThumbsUp();
		Docs docs = getSavedDocsNotReadonly();
		String contents = FixtureGenerator.getDefaultStringBuilder().sample();
		VersionDocs versionDocs = getSavedVersionDocs(docs, user);

		// when
		BumawikiException exception = assertThrows(BumawikiException.class,
			() -> commandDocsService.update(user, docs.getTitle(), contents, versionDocs.getVersion() + 1)
		);

		assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.DOCS_CONFLICTED);
	}

	@RepeatedTest(REPEAT_COUNT)
	void 문서_이름_수정() {
		//given
		Docs savedDocs = getSavedDocs();
		String newTitle = getStringWithout(savedDocs.getTitle());

		//when
		commandDocsService.titleUpdate(savedDocs.getTitle(), newTitle);

		//then
		assertThat(savedDocs.getTitle()).isEqualTo(newTitle);
	}

	@RepeatedTest(REPEAT_COUNT)
	void 있는_문서_이름_수정_실패() {
		//given
		String title = getTitle();
		getSavedDocs(title);
		Docs updatingDocs = getSavedDocs(getStringWithout(title));
		//when, then
		BumawikiException exception = assertThrows(BumawikiException.class,
			() -> commandDocsService.titleUpdate(updatingDocs.getTitle(), title)
		);

		assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.DOCS_TITLE_ALREADY_EXIST);
	}

	private static String getTitle() {
		return FixtureGenerator.getDefaultStringBuilder().ofMaxLength(32).sample();
	}

	private static String getStringWithout(String title) {
		String newTitle = getTitle();
		while (title.equals(newTitle)) {
			newTitle = getTitle();
		}

		return newTitle;
	}

	private Docs getDocs() {
		return FixtureGenerator.getDefaultDocsBuilder().sample();
	}

	@RepeatedTest(REPEAT_COUNT)
	void 문서_충돌_해결() {
		// given
		Docs docs = getSavedDocs();
		User user = getSavedUserWithOutThumbsUp();
		VersionDocs versionDocs = getSavedVersionDocs(docs, user);
		String contents = FixtureGenerator.getDefaultStringBuilder().sample();

		// when
		commandDocsService.solveConflict(docs.getTitle(), contents, versionDocs.getVersion(), user);

		// then
		assertThat(
			versionDocsRepository.findFirstByDocsOrderByVersionDesc(docs).getVersion()
		).isEqualTo(versionDocs.getVersion() + 1);
	}

	@RepeatedTest(REPEAT_COUNT)
	void 좋아요_내림차순_문서_전체_조회() {
		// given
		User user = userRepository.save(
			FixtureGenerator.getDefaultUserBuilder()
				.sample()
		);
		List<Docs> docs = docsRepository.saveAll(
			FixtureGenerator.getDefaultDocsBuilder()
				.sampleList(5)
		);

		for (Docs doc : docs) {
			thumbsUpRepository.saveAll(
				FixtureGenerator.getDefaultThumbsUpBuilder(doc, user)
					.sampleList(Arbitraries.integers().between(1, 30).sample())
			);
		}

		// when
		List<DocsPopularResponseDto> list = queryDocsService.readByThumbsUpsDesc();

		// then
		assertThat(
			thumbsUpRepository.findByDocs_Id(docsRepository.findByTitle(list.get(0).title()).get().getId())
		).hasSize(list.get(0).thumbsUpsCounts().intValue());
	}

	private void getSavedThumbsUpList(Docs docs, User user) {
		thumbsUpRepository.saveAll(
			FixtureGenerator.getDefaultThumbsUpBuilder(docs, user)
				.sampleList(5)
		);
	}

	private Docs getDocs(String title) {
		return FixtureGenerator.getDefaultDocsBuilder()
			.set(javaGetter(Docs::getTitle), title)
			.sample();
	}

	private Docs getSavedDocs() {
		return docsRepository.save(getDocs());
	}

	private Docs getSavedDocsNotReadonly() {
		return docsRepository.save(
			FixtureGenerator.getDefaultDocsBuilder()
				.setPostCondition(javaGetter(Docs::getDocsType), DocsType.class, it -> it != DocsType.READONLY)
				.sample()
		);
	}

	private Docs getSavedDocsReadonly() {
		return docsRepository.save(
			FixtureGenerator.getDefaultDocsBuilder()
				.set(javaGetter(Docs::getDocsType), DocsType.READONLY)
				.sample()
		);
	}

	private Docs getSavedNotStudentDocs(String name) {
		return docsRepository.save(
			FixtureGenerator.getDefaultDocsBuilder()
				.setPostCondition(javaGetter(Docs::getDocsType), DocsType.class, it -> it != DocsType.STUDENT)
				.set("title", name + FixtureGenerator.getDefaultStringBuilder().ofMaxLength(29).sample())
				.sample()
		);
	}

	private Docs getSavedDocs(String title) {
		return docsRepository.save(getDocs(title));
	}

	private VersionDocs getSavedVersionDocs(Docs docs, User user) {
		return versionDocsRepository.save(
			FixtureGenerator.getDefaultVersionDocsBuilder(docs, user)
				.set(javaGetter(VersionDocs::getVersion), 0)
				.sample()
		);
	}

	private User getSavedUserWithOutThumbsUp() {
		return userRepository.save(
			FixtureGenerator.getDefaultUserBuilder()
				.sample()
		);
	}

	private User getSavedUserWithOutThumbsUp(String name) {
		return userRepository.save(
			FixtureGenerator.getDefaultUserBuilder()
				.set(javaGetter(User::getName), name)
				.sample()
		);
	}
}
