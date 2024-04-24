package com.project.bumawiki.domain.user.service;

import org.springframework.stereotype.Service;

import com.project.bumawiki.domain.user.domain.User;
import com.project.bumawiki.domain.user.domain.authority.Authority;
import com.project.bumawiki.domain.user.implementation.UserReader;
import com.project.bumawiki.domain.user.implementation.UserUpdater;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommandUserService {
	private final UserReader userReader;
	private final UserUpdater userUpdater;

	public void updateUserAuthority(Long id, Authority authority) {
		User user = userReader.getById(id);
		userUpdater.updateAuthority(user, authority);
	}

}
