package com.project.bumawiki.domain.auth.repository;

import org.springframework.stereotype.Repository;
import org.springframework.web.context.annotation.RequestScope;

import com.project.bumawiki.domain.user.domain.User;
import com.project.bumawiki.global.error.exception.BumawikiException;
import com.project.bumawiki.global.error.exception.ErrorCode;

@Repository
@RequestScope
public class AuthRepository {
	private User currentUser;

	public User getCurrentUser() {
		if (currentUser == null) {
			throw new BumawikiException(ErrorCode.USER_NOT_LOGIN);
		}
		return currentUser;
	}

	public User getNullableCurrentUser() {
		return currentUser;
	}

	public void updateCurrentUser(User currentUser) {
		this.currentUser = currentUser;
	}
}
