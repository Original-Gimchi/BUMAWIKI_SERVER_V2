package com.project.bumawiki.domain.thumbsup.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.bumawiki.domain.docs.domain.Docs;
import com.project.bumawiki.domain.docs.domain.repository.DocsRepositoryMapper;
import com.project.bumawiki.domain.thumbsup.domain.ThumbsUp;
import com.project.bumawiki.domain.thumbsup.presentation.dto.ThumbsUpRequestDto;
import com.project.bumawiki.domain.user.domain.User;
import com.project.bumawiki.domain.user.domain.repository.UserRepository;
import com.project.bumawiki.domain.user.exception.UserNotFoundException;
import com.project.bumawiki.global.error.exception.ErrorCode;
import com.project.bumawiki.global.util.SecurityUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ThumbsUpManipulateService {
	private final DocsRepositoryMapper docsRepositoryMapper;
	private final UserRepository userRepository;

	@Transactional
	public void createDocsThumbsUp(ThumbsUpRequestDto likeRequestDto) {
		User user = getUser();
		Docs foundDocs = getDocs(likeRequestDto);

		addThumbsUp(foundDocs, user);
	}

	@Transactional
	public void removeLike(ThumbsUpRequestDto likeRequestDto) {
		User user = getUser();
		Docs foundDocs = getDocs(likeRequestDto);

		cancelThumbsUp(foundDocs, user);
	}

	//Like 추가
	private void addThumbsUp(Docs foundDocs, User user) {
		ThumbsUp thumbsUp = createThumbsUp(foundDocs, user);
		foundDocs.addThumbsUp(thumbsUp);
		user.addThumbsUp(thumbsUp);
	}

	//Like 삭제
	private void cancelThumbsUp(Docs foundDocs, User user) {
		ThumbsUp thumbsUpToDelete = createThumbsUp(foundDocs, user);
		foundDocs.cancelThumbsUp(thumbsUpToDelete);
		user.cancelThumbsUp(thumbsUpToDelete);
	}

	//Docs, User Like 만들기
	private ThumbsUp createThumbsUp(Docs foundDocs, User user) {
		return ThumbsUp.builder()
			.docs(foundDocs)
			.user(user)
			.build();
	}

	//Docs, User 가져오기
	private Docs getDocs(ThumbsUpRequestDto likeRequestDto) {
		return docsRepositoryMapper.findById(
			likeRequestDto.getDocsId(),
			ErrorCode.NO_DOCS_YOU_THUMBS_UP
		);
	}

	private User getUser() {
		Long userId = SecurityUtil
			.getCurrentUserWithLogin()
			.getId();

		return userRepository.findById(userId)
			.orElseThrow(() -> UserNotFoundException.EXCEPTION);
	}
}
