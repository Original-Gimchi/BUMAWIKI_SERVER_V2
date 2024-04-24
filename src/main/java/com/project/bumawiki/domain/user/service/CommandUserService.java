package com.project.bumawiki.domain.user.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.bumawiki.domain.user.domain.User;
import com.project.bumawiki.domain.user.domain.authority.Authority;
import com.project.bumawiki.domain.user.implementation.UserReader;
import com.project.bumawiki.domain.user.implementation.UserUpdater;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class CommandUserService {
	private final UserReader userReader;
	private final UserUpdater userUpdater;

	public void updateUserAuthority(Long id, Authority authority) {
		System.out.println("updateUserAuthority");
		User user = userReader.getById(id);
		System.out.println("updateUserAuthority2");
		userUpdater.updateAuthority(user, authority);
		System.out.println("updateUserAuthority3");
	}

}
