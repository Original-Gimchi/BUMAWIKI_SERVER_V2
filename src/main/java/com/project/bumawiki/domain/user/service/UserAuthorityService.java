package com.project.bumawiki.domain.user.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.bumawiki.domain.user.domain.User;
import com.project.bumawiki.domain.user.domain.authority.Authority;
import com.project.bumawiki.domain.user.domain.repository.UserRepositoryMapper;
import com.project.bumawiki.domain.user.presentation.dto.UserAuthorityDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class UserAuthorityService {
	private final UserRepositoryMapper userRepositoryMapper;

	public Authority execute(final UserAuthorityDto userAuthorityDto) {
		User user = userRepositoryMapper.getByEmail(userAuthorityDto.getEmail());
		user.changeUserAuthority(userAuthorityDto.getAuthority());
		return user.getAuthority();
	}

}
