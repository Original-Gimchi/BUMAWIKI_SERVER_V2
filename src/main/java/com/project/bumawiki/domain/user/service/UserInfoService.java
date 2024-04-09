package com.project.bumawiki.domain.user.service;

import com.project.bumawiki.domain.user.domain.User;
import com.project.bumawiki.domain.user.domain.repository.UserRepositoryMapper;
import com.project.bumawiki.global.annotation.ServiceWithTransactionalReadOnly;
import com.project.bumawiki.global.util.SecurityUtil;

import lombok.RequiredArgsConstructor;

@ServiceWithTransactionalReadOnly
@RequiredArgsConstructor
public class UserInfoService {

	private final UserRepositoryMapper userRepositoryMapper;

	public User getLoginUser() {
		Long id = SecurityUtil.getCurrentUserWithLogin().getId();
		return userRepositoryMapper.getById(id);
	}

	public User findAnotherInfo(Long userId) {
		return userRepositoryMapper.getById(userId);
	}
}
