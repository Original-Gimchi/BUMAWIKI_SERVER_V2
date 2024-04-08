package com.project.bumawiki.domain.docs.exception;

import com.project.bumawiki.global.error.exception.BumawikiException;
import com.project.bumawiki.global.error.exception.ErrorCode;

public class VersionNotExistException extends BumawikiException {

	public static final NoUpdatableDocsException EXCEPTION = new NoUpdatableDocsException(ErrorCode.VERSION_NOT_EXIST);

	public VersionNotExistException(ErrorCode errorCode) {
		super(errorCode);
	}
}
