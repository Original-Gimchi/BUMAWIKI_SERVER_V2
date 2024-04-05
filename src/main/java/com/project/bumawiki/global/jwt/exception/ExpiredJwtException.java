package com.project.bumawiki.global.jwt.exception;

import com.project.bumawiki.global.error.exception.BumawikiException;
import com.project.bumawiki.global.error.exception.ErrorCode;

public class ExpiredJwtException extends BumawikiException {
	public static final ExpiredJwtException EXCEPTION = new ExpiredJwtException(ErrorCode.EXPIRED_JWT);

	public ExpiredJwtException(ErrorCode errorCode) {
		super(errorCode);
	}
}
