package com.project.bumawiki.domain.thumbsup.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

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

public class CommandThumbsUpServiceTest extends ServiceTest {
	@Autowired
	private CommandThumbsUpService commandThumbsUpService;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ThumbsUpRepository thumbsUpRepository;
	@Autowired
	private DocsRepository docsRepository;

	@Nested
	class 좋아요_생성 {
		@Test
		void 생성() {
			//given
			User user = FixtureGenerator.getDefaultUserBuilder().sample();
			Docs docs = FixtureGenerator.getDefaultDocsBuilder().sample();
			userRepository.save(user);
			docsRepository.save(docs);

			//when
			commandThumbsUpService.createThumbsUp(user, docs.getId());

			//then
			assertThat(thumbsUpRepository.existsByDocs_IdAndUser(docs.getId(), user)).isTrue();
		}

		@Test
		void 이미_좋아요_생성된_문서_좋아요_생성_실패() {
			//given
			User user = FixtureGenerator.getDefaultUserBuilder().sample();
			Docs docs = FixtureGenerator.getDefaultDocsBuilder().sample();
			userRepository.save(user);
			docsRepository.save(docs);
			thumbsUpRepository.save(new ThumbsUp(user, docs));

			//when, then
			BumawikiException bumawikiException = assertThrows(BumawikiException.class,
				() -> commandThumbsUpService.createThumbsUp(user, docs.getId()));

			assertThat(bumawikiException.getErrorCode()).isEqualTo(ErrorCode.ALREADY_THUMBS_UP);
		}
	}

	@Nested
	class 좋아요_삭제 {
		@Test
		void 삭제() {
			//given
			User user = FixtureGenerator.getDefaultUserBuilder().sample();
			Docs docs = FixtureGenerator.getDefaultDocsBuilder().sample();
			userRepository.save(user);
			docsRepository.save(docs);
			thumbsUpRepository.save(new ThumbsUp(user, docs));

			//when
			commandThumbsUpService.cancelThumbsUp(user, docs.getId());

			//then
			assertThat(thumbsUpRepository.existsByDocs_IdAndUser(docs.getId(), user)).isFalse();
		}

		@Test
		void 안좋아하는_문서_좋아요_삭제_실패() {
			//given
			User user = FixtureGenerator.getDefaultUserBuilder().sample();
			Docs docs = FixtureGenerator.getDefaultDocsBuilder().sample();
			userRepository.save(user);
			docsRepository.save(docs);

			//when, then
			BumawikiException bumawikiException = assertThrows(BumawikiException.class,
				() -> commandThumbsUpService.cancelThumbsUp(user, docs.getId()));

			assertThat(bumawikiException.getErrorCode()).isEqualTo(ErrorCode.YOU_DONT_THUMBS_UP_THIS_DOCS);
		}
	}
}
