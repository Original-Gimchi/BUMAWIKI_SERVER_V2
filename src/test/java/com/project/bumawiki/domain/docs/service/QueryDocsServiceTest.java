package com.project.bumawiki.domain.docs.service;

import static com.navercorp.fixturemonkey.api.experimental.JavaGetterMethodPropertySelector.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.RepeatedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;

import net.jqwik.api.Arbitraries;

import com.project.bumawiki.domain.docs.domain.Docs;
import com.project.bumawiki.domain.docs.domain.VersionDocs;
import com.project.bumawiki.domain.docs.domain.repository.DocsRepository;
import com.project.bumawiki.domain.docs.domain.repository.VersionDocsRepository;
import com.project.bumawiki.domain.docs.domain.type.DocsType;
import com.project.bumawiki.domain.docs.presentation.dto.response.ClubResponseDto;
import com.project.bumawiki.domain.docs.presentation.dto.response.DocsNameAndEnrollResponseDto;
import com.project.bumawiki.domain.docs.presentation.dto.response.DocsPopularResponseDto;
import com.project.bumawiki.domain.docs.presentation.dto.response.DocsResponseDto;
import com.project.bumawiki.domain.docs.presentation.dto.response.MergeConflictDataResponseDto;
import com.project.bumawiki.domain.docs.presentation.dto.response.TeacherResponseDto;
import com.project.bumawiki.domain.docs.presentation.dto.response.VersionResponseDto;
import com.project.bumawiki.domain.docs.util.DocsUtil;
import com.project.bumawiki.domain.thumbsup.domain.repository.ThumbsUpRepository;
import com.project.bumawiki.domain.user.domain.User;
import com.project.bumawiki.domain.user.domain.repository.UserRepository;
import com.project.bumawiki.global.error.exception.BumawikiException;
import com.project.bumawiki.global.error.exception.ErrorCode;
import com.project.bumawiki.global.service.FixtureGenerator;
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

	@Autowired
	private ThumbsUpRepository thumbsUpRepository;

	@RepeatedTest(REPEAT_COUNT)
	void 제목으로_문서_상세_조회하기() {

		User user = FixtureGenerator.getDefaultUserBuilder().sample();

		userRepository.save(user);

		Docs docs = fixtureGenerator.giveMeBuilder(Docs.class).setNull("id").setNull("versionDocs").sample();
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
		assertAll(() -> {
			assertThat(dto).isNotNull();
			assertThat(dto.title()).isEqualTo(title);
		});
	}

	@RepeatedTest(REPEAT_COUNT)
	void 없는_문서_조회_시_예외_발생() {
		//given
		String title = Arbitraries.strings().ofMinLength(0).sample();
		//when
		BumawikiException exception = assertThrows(BumawikiException.class, () -> queryDocsService.findDocs(title));

		//then
		assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.DOCS_NOT_FOUND);
	}

	@RepeatedTest(REPEAT_COUNT)
	void 없는_버전_조회_시_Exception() {
		//given
		Docs docs = FixtureGenerator.getDefaultDocsBuilder().sample();
		String title = docs.getTitle();
		docsRepository.save(docs);
		//when
		BumawikiException exception = assertThrows(BumawikiException.class,
			() -> queryDocsService.showVersionDocsDiff(title, 0));

		//then
		assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.VERSION_NOT_EXIST);
	}

	@RepeatedTest(REPEAT_COUNT)
	void 제목으로_문서_역사_조회하기() {
		// given
		User user = FixtureGenerator.getDefaultUserBuilder()
			.sample();

		userRepository.save(user);

		Docs docs = FixtureGenerator.getDefaultDocsBuilder().sample();
		String title = docs.getTitle();

		docsRepository.save(docs);

		List<VersionDocs> versionDocsList = FixtureGenerator.getDefaultVersionDocsBuilder(docs, user)
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
			() -> assertThat(dto.title()).isEqualTo(title));
	}

	@RepeatedTest(REPEAT_COUNT)
	void 문서_타입으로_조회하기() {
		// given
		User user = FixtureGenerator.getDefaultUserBuilder()
			.sample();

		userRepository.save(user);

		DocsType randomDocsType = Arbitraries.of(DocsType.class).sample();

		List<Docs> docsList = FixtureGenerator.getDefaultDocsBuilder().sampleList(30);
		docsList.add(FixtureGenerator.getDefaultDocsBuilder()
			.set(javaGetter(Docs::getDocsType), randomDocsType).sample());
		docsRepository.saveAll(docsList);
		// when
		List<Docs> findDocsList = queryDocsService.findByDocsTypeOrderByEnroll(randomDocsType);

		// then
		assertAll(
			() -> assertThat(docsList.containsAll(findDocsList)).isTrue(),
			() -> assertThat(
				findDocsList.stream().map(Docs::getDocsType).collect(Collectors.toSet()).size()
			).isEqualTo(1),
			() -> assertThat(
				findDocsList.stream().map(Docs::getDocsType).collect(Collectors.toSet()).contains(randomDocsType)
			).isTrue()
		);
	}

	@RepeatedTest(REPEAT_COUNT)
	void 유저가_수정한_버전문서_조회() {
		// given
		User user = FixtureGenerator.getDefaultUserBuilder()
			.sample();

		User otherUser = FixtureGenerator.getDefaultUserBuilder()
			.sample();

		userRepository.save(user);
		userRepository.save(otherUser);

		Docs docs = FixtureGenerator.getDefaultDocsBuilder().sample();

		docsRepository.save(docs);

		List<VersionDocs> versionDocsList = FixtureGenerator.getDefaultVersionDocsBuilder(docs, user)
			.sampleList(10);

		List<VersionDocs> versionDocsList2 = FixtureGenerator.getDefaultVersionDocsBuilder(docs, user)
			.sampleList(10);

		versionDocsRepository.saveAll(versionDocsList);
		versionDocsRepository.saveAll(versionDocsList2);

		// when
		List<VersionDocs> findVersionDocsList = queryDocsService.findAllVersionDocsByUser(user);

		// then
		assertAll(
			() -> assertThat(
				findVersionDocsList.stream()
					.allMatch(versionDocs -> versionDocs.getUser().getId().equals(user.getId()))
			).isTrue()
		);

	}

	@RepeatedTest(REPEAT_COUNT)
	public void 최근_수정한_문서_10개_조회() {
		//given
		User user = FixtureGenerator.getDefaultUserBuilder().sample();
		userRepository.save(user);

		List<Docs> docsList = FixtureGenerator.getDefaultDocsBuilder()
			.setNotNull(javaGetter(Docs::getLastModifiedAt))
			.sampleList(20);

		docsRepository.saveAll(docsList);

		List<VersionDocs> versionDocsList = FixtureGenerator.getDefaultVersionDocsBuilder(null, user)
			.set(javaGetter(VersionDocs::getDocs), Arbitraries.of(docsList))
			.setNotNull(javaGetter(VersionDocs::getCreatedAt))
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
			() -> assertThat(findDocsList.stream().map(Docs::getId).toList()).containsAll(finalSortedDocsList),
			() -> assertThat(findDocsList.size()).isLessThanOrEqualTo(10));

	}

	@RepeatedTest(REPEAT_COUNT)
	void 모든_선생님_문서_조회하기() {
		// given
		User user = FixtureGenerator.getDefaultUserBuilder().sample();

		userRepository.save(user);

		List<Docs> docsList = FixtureGenerator.getDefaultDocsBuilder()
			.sampleList(30);

		docsRepository.saveAll(docsList);

		docsList.forEach(docs -> {
			VersionDocs versionDocs = FixtureGenerator.getDefaultVersionDocsBuilder(docs, user)
				.sample();
			docs.getVersionDocs().add(versionDocs);
			versionDocsRepository.save(versionDocs);
		});

		// when
		TeacherResponseDto dto = sortTeacherResponseDto(queryDocsService.getAllTeacher());

		// then
		List<DocsNameAndEnrollResponseDto> teacherList = getDocsListByDocsType(docsList, DocsType.TEACHER);
		List<DocsNameAndEnrollResponseDto> majorTeacherList = getDocsListByDocsType(docsList, DocsType.MAJOR_TEACHER);
		List<DocsNameAndEnrollResponseDto> mentorTeacherList = getDocsListByDocsType(docsList, DocsType.MENTOR_TEACHER);

		TeacherResponseDto toComparedDto = new TeacherResponseDto(teacherList, majorTeacherList, mentorTeacherList);

		assertThat(dto.equals(toComparedDto)).isTrue();
	}

	@RepeatedTest(REPEAT_COUNT)
	void 모든_동아리_문서_조회하기() {
		// given
		User user = FixtureGenerator.getDefaultUserBuilder().sample();

		userRepository.save(user);

		List<Docs> docsList = FixtureGenerator.getDefaultDocsBuilder()
			.sampleList(30);

		docsRepository.saveAll(docsList);

		docsList.forEach(docs -> {
			VersionDocs versionDocs = FixtureGenerator.getDefaultVersionDocsBuilder(docs, user)
				.sample();
			docs.getVersionDocs().add(versionDocs);
			versionDocsRepository.save(versionDocs);
		});

		// when
		ClubResponseDto dto = sortClubResponseDto(queryDocsService.getAllClub());

		// then
		List<DocsNameAndEnrollResponseDto> clubList = getDocsListByDocsType(docsList, DocsType.CLUB);
		List<DocsNameAndEnrollResponseDto> freeClubList = getDocsListByDocsType(docsList, DocsType.FREE_CLUB);

		ClubResponseDto toComparedDto = new ClubResponseDto(clubList, freeClubList);

		assertThat(dto.equals(toComparedDto)).isTrue();
	}

	@RepeatedTest(100)
	void 좋아요_내림차순_문서_전체_조회() {
		// given
		User user = FixtureGenerator.getDefaultUserBuilder().sample();
		userRepository.save(user);
		List<Docs> docs = FixtureGenerator.getDefaultDocsBuilder().sampleList(5);

		docsRepository.saveAll(docs);

		docs.forEach(it ->
			thumbsUpRepository.saveAll(
				FixtureGenerator.getDefaultThumbsUpBuilder(it, user)
					.sampleList(Arbitraries.integers().between(1, 30).sample())
			)
		);

		// when
		List<DocsPopularResponseDto> list = queryDocsService.readByThumbsUpsDesc();

		// then
		assertThat(
			thumbsUpRepository.findByDocs_Id(docsRepository.findByTitle(list.get(0).title()).get().getId())
		).hasSize(list.get(0).thumbsUpsCounts().intValue());
	}

	@RepeatedTest(100)
	void 문서_충돌_조회() {
		//given
		Docs docs = FixtureGenerator.getDefaultDocsBuilder().sample();

		docsRepository.save(docs);

		User user = FixtureGenerator.getDefaultUserBuilder().sample();

		userRepository.save(user);

		String firstContents = FixtureGenerator.getDefaultStringBuilder().sample();
		String secondContents = FixtureGenerator.getDefaultStringBuilder()
			.filter(s -> !s.equals(firstContents))
			.sample();
		String thirdContents = FixtureGenerator.getDefaultStringBuilder()
			.filter(s -> !s.equals(firstContents) && !s.equals(secondContents))
			.sample();

		versionDocsRepository.save(new VersionDocs(0, docs, firstContents, user));
		versionDocsRepository.save(new VersionDocs(1, docs, secondContents, user));
		//when
		MergeConflictDataResponseDto mergeConflict = queryDocsService.getMergeConflict(docs.getTitle(), thirdContents);
		//then
		assertAll(
			() -> assertThat(mergeConflict.originalDocsContent()).isEqualTo(firstContents),
			() -> assertThat(mergeConflict.firstDocsContent()).isEqualTo(secondContents),
			() -> assertThat(mergeConflict.diff1().size()).isEqualTo(
				DocsUtil.getDiff(firstContents, secondContents).size()),
			() -> assertThat(mergeConflict.diff2().size()).isEqualTo(
				DocsUtil.getDiff(firstContents, thirdContents).size())
		);
	}

	private List<DocsNameAndEnrollResponseDto> getDocsListByDocsType(List<Docs> docsList, DocsType docsType) {
		return docsList.stream()
			.filter(docs -> docs.getDocsType().equals(docsType))
			.map(DocsNameAndEnrollResponseDto::new)
			.toList();
	}

	private TeacherResponseDto sortTeacherResponseDto(TeacherResponseDto dto) {
		List<DocsNameAndEnrollResponseDto> teacher = dto.teacher()
			.stream()
			.sorted(Comparator.comparing(DocsNameAndEnrollResponseDto::id))
			.toList();
		List<DocsNameAndEnrollResponseDto> majorTeacher = dto.majorTeacher()
			.stream()
			.sorted(Comparator.comparing(DocsNameAndEnrollResponseDto::id))
			.toList();
		List<DocsNameAndEnrollResponseDto> mentorTeacher = dto.mentorTeacher()
			.stream()
			.sorted(Comparator.comparing(DocsNameAndEnrollResponseDto::id))
			.toList();
		return new TeacherResponseDto(teacher, majorTeacher, mentorTeacher);
	}

	private ClubResponseDto sortClubResponseDto(ClubResponseDto dto) {
		List<DocsNameAndEnrollResponseDto> club = dto.club()
			.stream()
			.sorted(Comparator.comparing(DocsNameAndEnrollResponseDto::id))
			.toList();
		List<DocsNameAndEnrollResponseDto> freeClub = dto.freeClub()
			.stream()
			.sorted(Comparator.comparing(DocsNameAndEnrollResponseDto::id))
			.toList();
		return new ClubResponseDto(club, freeClub);
	}
}
