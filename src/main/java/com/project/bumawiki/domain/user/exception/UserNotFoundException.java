package com.project.bumawiki.domain.user.exception;

import com.project.bumawiki.global.error.exception.BumawikiException;
import com.project.bumawiki.global.error.exception.ErrorCode;

public class UserNotFoundException extends BumawikiException {

	public static final UserNotFoundException EXCEPTION = new UserNotFoundException(ErrorCode.USER_NOT_FOUND);

	public UserNotFoundException(ErrorCode errorCode) {
		super(errorCode);
	}
}
