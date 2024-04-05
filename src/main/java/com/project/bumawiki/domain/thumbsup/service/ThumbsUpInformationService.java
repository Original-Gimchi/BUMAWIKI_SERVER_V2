package com.project.bumawiki.domain.thumbsup.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.bumawiki.domain.thumbsup.domain.repository.CustomThumbsUpRepository;
import com.project.bumawiki.domain.thumbsup.presentation.dto.ThumbsUpResponseDto;
import com.project.bumawiki.domain.user.domain.User;
import com.project.bumawiki.global.util.SecurityUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ThumbsUpInformationService {

	private final CustomThumbsUpRepository customThumbsUpRepository;

	public List<ThumbsUpResponseDto> getThumbsUpList() {
		User user = SecurityUtil.getCurrentUserOrNotLogin();

		return customThumbsUpRepository.getUserThumbsUp(user);
	}
}

