package com.project.bumawiki.domain.user.service;

import org.springframework.stereotype.Service;

import com.project.bumawiki.domain.user.domain.User;
import com.project.bumawiki.domain.user.implementation.UserReader;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QueryUserService {

	private final UserReader userReader;

	public User findUserInfo(Long userId) {
		return userReader.getById(userId);
	}
}
