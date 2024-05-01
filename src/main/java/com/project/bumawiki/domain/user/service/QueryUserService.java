package com.project.bumawiki.domain.user.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.bumawiki.domain.user.domain.User;
import com.project.bumawiki.domain.user.implementation.UserReader;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class QueryUserService {

	private final UserReader userReader;

	public User findUserInfo(Long userId) {
		return userReader.getById(userId);
	}
}
