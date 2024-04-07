package com.project.bumawiki.domain.thumbsup.exception;

import com.project.bumawiki.global.error.exception.BumawikiException;
import com.project.bumawiki.global.error.exception.ErrorCode;

public class AlreadyThumbsUpException extends BumawikiException {

	public static final AlreadyThumbsUpException EXCEPTION = new AlreadyThumbsUpException(ErrorCode.ALREADY_THUMBS_UP);

	private AlreadyThumbsUpException(ErrorCode errorCode) {
		super(errorCode);
	}
}
