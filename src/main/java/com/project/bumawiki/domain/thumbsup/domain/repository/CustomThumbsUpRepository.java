package com.project.bumawiki.domain.thumbsup.domain.repository;

import java.util.List;

import com.project.bumawiki.domain.thumbsup.presentation.dto.ThumbsUpResponseDto;
import com.project.bumawiki.domain.user.domain.User;

public interface CustomThumbsUpRepository {
	List<ThumbsUpResponseDto> getUserThumbsUp(User user);

	Long countThumbsUpByTitle(String title);
}
