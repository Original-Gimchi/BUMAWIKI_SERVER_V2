package com.project.bumawiki.domain.user.service;

import com.project.bumawiki.domain.user.domain.User;
import com.project.bumawiki.domain.user.domain.repository.UserRepositoryMapper;
import com.project.bumawiki.global.annotation.ServiceWithTransactionalReadOnly;

import lombok.RequiredArgsConstructor;

@ServiceWithTransactionalReadOnly
@RequiredArgsConstructor
public class UserInfoService {

	private final UserRepositoryMapper userRepositoryMapper;

	public User findAnotherInfo(Long userId) {
		return userRepositoryMapper.getById(userId);
	}
}
