package com.project.bumawiki.global.util;

import org.springframework.security.core.context.SecurityContextHolder;

import com.project.bumawiki.domain.user.domain.User;
import com.project.bumawiki.domain.user.exception.UserNotLoginException;
import com.project.bumawiki.global.config.security.auth.AuthDetails;

public class SecurityUtil {
	public static User getCurrentUserWithLogin() {
		try {
			return getUser();
		} catch (ClassCastException e) {
			throw UserNotLoginException.EXCEPTION;
		}
	}

	public static User getCurrentUserOrNotLogin() {
		return getUser();
	}

	private static User getUser() {

		Object principal = SecurityContextHolder.getContext()
			.getAuthentication()
			.getPrincipal();

		if (principal instanceof String) {
			throw UserNotLoginException.EXCEPTION;
		}

		AuthDetails authDetails = (AuthDetails)principal;

		return authDetails.getUser();
	}
}
