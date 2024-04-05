package com.project.bumawiki.global.jwt.exception;

import com.project.bumawiki.global.error.exception.BumawikiException;
import com.project.bumawiki.global.error.exception.ErrorCode;

public class RefreshTokenExpiredException extends BumawikiException {

	public static final RefreshTokenExpiredException EXCEPTION = new RefreshTokenExpiredException(
		ErrorCode.REFRESH_TOKEN_EXPIRED);

	public RefreshTokenExpiredException(ErrorCode errorCode) {
		super(errorCode);
	}
}
