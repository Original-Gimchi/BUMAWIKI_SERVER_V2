package com.project.bumawiki.domain.thumbsup.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.RepeatedTest;
import org.springframework.beans.factory.annotation.Autowired;

import net.jqwik.api.Arbitraries;

import com.project.bumawiki.domain.docs.domain.Docs;
import com.project.bumawiki.domain.docs.domain.repository.DocsRepository;
import com.project.bumawiki.domain.thumbsup.domain.ThumbsUp;
import com.project.bumawiki.domain.thumbsup.domain.repository.ThumbsUpRepository;
import com.project.bumawiki.domain.user.domain.User;
import com.project.bumawiki.domain.user.domain.repository.UserRepository;
import com.project.bumawiki.global.error.exception.BumawikiException;
import com.project.bumawiki.global.error.exception.ErrorCode;
import com.project.bumawiki.global.service.FixtureGenerator;
import com.project.bumawiki.global.service.ServiceTest;

public class QueryThumbsUpServiceTest extends ServiceTest {
	@Autowired
	private QueryThumbsUpService queryThumbsUpService;
	@Autowired
	private DocsRepository docsRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ThumbsUpRepository thumbsUpRepository;

	@RepeatedTest(REPEAT_COUNT)
	void 문서에_눌린_좋아요_개수_조회() {
		//given
		Integer size = Arbitraries.integers().between(0, 100).sample();
		List<User> users = FixtureGenerator.getDefaultUserBuilder().sampleList(size);
		Docs docs = FixtureGenerator.getDefaultDocsBuilder().sample();
		userRepository.saveAll(users);
		docsRepository.save(docs);

		for (User user : users) {
			thumbsUpRepository.save(new ThumbsUp(user, docs));
		}

		//when, then
		assertThat(queryThumbsUpService.countThumbsUpByDocsTitle(docs.getTitle()))
			.isEqualTo(users.size());
	}

	@RepeatedTest(REPEAT_COUNT)
	void 좋아요_전체_가져오기() {
		//given
		Integer size = Arbitraries.integers().between(0, 100).sample();
		User user = FixtureGenerator.getDefaultUserBuilder().sample();
		List<Docs> allDocs = FixtureGenerator.getDefaultDocsBuilder().sampleList(size);
		userRepository.save(user);
		docsRepository.saveAll(allDocs);

		for (Docs docs : allDocs) {
			thumbsUpRepository.save(new ThumbsUp(user, docs));
		}

		//when
		List<Docs> thumbsUp = queryThumbsUpService.getThumbsUp(user);

		//then
		assertThat(thumbsUp.size()).isEqualTo(allDocs.size());
	}

	@Nested
	class 문서_좋아하는지_확인 {
		@RepeatedTest(REPEAT_COUNT)
		void 확인() {
			//given
			Docs docs = FixtureGenerator.getDefaultDocsBuilder().sample();
			User user = FixtureGenerator.getDefaultUserBuilder().sample();
			docsRepository.save(docs);
			userRepository.save(user);
			//when
			boolean shouldFalse = queryThumbsUpService.checkUserLikeThisDocs(docs.getId(), user);

			thumbsUpRepository.save(new ThumbsUp(user, docs));
			boolean shouldTrue = queryThumbsUpService.checkUserLikeThisDocs(docs.getId(), user);
			//then
			assertAll(
				() -> assertFalse(shouldFalse),
				() -> assertTrue(shouldTrue)
			);
		}

		@RepeatedTest(REPEAT_COUNT)
		void 조회하려는_문서가_없다면_실패() {
			//given
			User user = FixtureGenerator.getDefaultUserBuilder().sample();
			Long docsId = fixtureGenerator.giveMeOne(Long.class);
			userRepository.save(user);

			//when, then
			BumawikiException bumawikiException = assertThrows(BumawikiException.class,
				() -> queryThumbsUpService.checkUserLikeThisDocs(docsId, user));

			assertThat(bumawikiException.getErrorCode()).isEqualTo(ErrorCode.DOCS_NOT_FOUND);
		}
	}

}
