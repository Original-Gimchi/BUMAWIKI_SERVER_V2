package com.project.bumawiki.global.jwt.exception;

import com.project.bumawiki.global.error.exception.BumawikiException;
import com.project.bumawiki.global.error.exception.ErrorCode;

public class InvalidJwtException extends BumawikiException {

	public static InvalidJwtException EXCEPTION = new InvalidJwtException(ErrorCode.INVALID_TOKEN);

	public InvalidJwtException(ErrorCode errorCode) {
		super(errorCode);
	}
}
